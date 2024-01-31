package ru.nn;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nn.core.repository.executor.DbExecutorImpl;
import ru.nn.core.sessionmanager.TransactionRunnerJdbc;
import ru.nn.crm.datasource.DriverManagerDataSource;
import ru.nn.crm.model.Client;
import ru.nn.crm.model.Manager;
import ru.nn.crm.service.DbServiceClientImpl;
import ru.nn.crm.service.DbServiceManagerImpl;
import ru.nn.jdbc.mapper.*;

import javax.sql.DataSource;

@SuppressWarnings({"java:S125", "java:S1481"})
public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        // Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        // Работа с клиентом
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<Client>();
        EntitySQLMetaData entitySQLMetaDataClient  = new EntitySQLMetaDataImpl<Client>(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<Client>(
                dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient); // реализация DataTemplate, универсальная

        // Код дальше должен остаться
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

        // Сделайте тоже самое с классом Manager (для него надо сделать свою таблицу)

        EntityClassMetaData<Manager> entityClassMetaDataManager = new EntityClassMetaDataImpl<Manager>();
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl<Manager>(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<Manager>(
                dbExecutor, entitySQLMetaDataManager, entityClassMetaDataManager);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);
        dbServiceManager.saveManager(new Manager("ManagerFirst"));

        var managerSecond = dbServiceManager.saveManager(new Manager("ManagerSecond"));
        var managerSecondSelected = dbServiceManager
                .getManager(managerSecond.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + managerSecond.getNo()));
        log.info("managerSecondSelected:{}", managerSecondSelected);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
