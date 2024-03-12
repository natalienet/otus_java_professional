package ru.nn;

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
import ru.nn.services.TemplateProcessor;
import ru.nn.services.TemplateProcessorImpl;
import ru.nn.services.UserAuthService;
import ru.nn.services.UserAuthServiceImpl;

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

        UserDao userDao = new InMemoryUserDao();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        ClientsWebServer clientsWebServer = new ClientsWebServerWithFilterBasedSecurity(
                WEB_SERVER_PORT, authService, dbServiceClient, templateProcessor);

        clientsWebServer.start();
        clientsWebServer.join();
    }
}
