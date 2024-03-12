package ru.nn.server;

import com.google.gson.Gson;
import java.util.Arrays;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import ru.nn.crm.service.DBServiceClient;
import ru.nn.services.TemplateProcessor;
import ru.nn.services.UserAuthService;
import ru.nn.servlet.AuthorizationFilter;
import ru.nn.servlet.LoginServlet;

public class ClientsWebServerWithFilterBasedSecurity extends ClientsWebServerSimple {
    private final UserAuthService authService;

    public ClientsWebServerWithFilterBasedSecurity(
            int port, UserAuthService authService, DBServiceClient dbServiceClient, Gson gson,
            TemplateProcessor templateProcessor) {
        super(port, dbServiceClient, gson, templateProcessor);
        this.authService = authService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths)
                .forEachOrdered(
                        path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
