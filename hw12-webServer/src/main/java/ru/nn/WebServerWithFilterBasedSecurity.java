package ru.nn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import ru.nn.dao.InMemoryUserDao;
import ru.nn.dao.UserDao;
import ru.nn.server.ClientsWebServer;
import ru.nn.server.ClientsWebServerWithFilterBasedSecurity;
import ru.nn.services.*;

import java.util.List;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithFilterBasedSecurity {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final Logger log = LoggerFactory.getLogger(WebServerWithFilterBasedSecurity.class);
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
//
//        dbServiceClient.saveClient(new Client(null, "Ivan Petrov",
//                new Address(null, "Tverskaya street"),
//                List.of(new Phone(null, "13-555-22"))));
//
//        dbServiceClient.saveClient(new Client(null, "Petr Ivanov",
//                new Address(null, "Leninskiy prospekt"),
//                List.of(new Phone(null, "32-555-13"))));

        UserDao userDao = new InMemoryUserDao();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        ClientsWebServer clientsWebServer = new ClientsWebServerWithFilterBasedSecurity(
                WEB_SERVER_PORT, authService, dbServiceClient, gson, templateProcessor);

        clientsWebServer.start();
        clientsWebServer.join();
    }
}
