package ru.nn.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

import ru.nn.crm.model.Address;
import ru.nn.crm.model.Client;
import ru.nn.crm.model.Phone;
import ru.nn.crm.service.DBServiceClient;
import ru.nn.services.TemplateProcessor;

@SuppressWarnings({"squid:S1948"})
public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    //private static final String TEMPLATE_ATTR_RANDOM_USER = "randomUser";

    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<List<String>> clientsFields = new ArrayList<>();
        List<Client> clients = dbServiceClient.findAll();
        for (Client client : clients) {
            clientsFields.add(Arrays.asList(client.getId().toString(), client.getName(), client.getAddress().getStreet(),
                    client.getPhones().getFirst().getNumber()));
        }
        paramsMap.put("clients", clientsFields);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dbServiceClient.saveClient(extractClientFromRequest(req));
        doGet(req, resp);
    }

    private Client extractClientFromRequest(HttpServletRequest request) throws IOException {
        String name = request.getParameter("clientName");
        String address = request.getParameter("clientAddress");
        String phone = request.getParameter("clientPhone");

        return new Client(null, name, new Address(address), List.of(new Phone(phone)));
    }
}
