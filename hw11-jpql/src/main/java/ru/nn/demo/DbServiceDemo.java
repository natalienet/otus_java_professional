package ru.nn.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nn.core.repository.DataTemplateHibernate;
import ru.nn.core.repository.HibernateUtils;
import ru.nn.core.sessionmanager.TransactionManagerHibernate;
import ru.nn.crm.model.Address;
import ru.nn.crm.model.Client;
import ru.nn.crm.model.Phone;
import ru.nn.crm.service.DbServiceClientImpl;

import java.util.List;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
//
//        var dbUrl = configuration.getProperty("hibernate.connection.url");
//        var dbUserName = configuration.getProperty("hibernate.connection.username");
//        var dbPassword = configuration.getProperty("hibernate.connection.password");

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        ///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        ///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        dbServiceClient.saveClient(new Client(null, "dbServiceFirst", new Address(null, "AnyStreet1"), List.of(new Phone(null, "13-555-22"),
                new Phone(null, "14-666-333"))));

        var clientSecond = dbServiceClient.saveClient(new Client(null, "dbServiceSecond", new Address(null, "AnyStreet2"), List.of(new Phone(null, "11-222-33"))));
        var clientSecondSelected = dbServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);
        ///
        dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "dbServiceSecondUpdated"));
        var clientUpdated = dbServiceClient
                .getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}
