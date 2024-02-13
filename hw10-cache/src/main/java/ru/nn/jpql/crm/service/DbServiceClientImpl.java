package ru.nn.jpql.crm.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nn.jpql.core.repository.DataTemplate;
import ru.nn.jpql.core.sessionmanager.TransactionManager;
import ru.nn.jpql.crm.model.Client;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;

    Map<String, Client> cache;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        cache = new WeakHashMap<>();
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                var savedClient = clientDataTemplate.insert(session, clientCloned);
                cache.put(String.valueOf(savedClient.getId()), savedClient);
                log.info("created client: {}", clientCloned);
                return savedClient;
            }
            var savedClient = clientDataTemplate.update(session, clientCloned);
            cache.put(String.valueOf(savedClient.getId()), savedClient);
            log.info("updated client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        String idAsString = String.valueOf(id);
        if (cache.containsKey(idAsString)) {
            return Optional.of(cache.get(idAsString));
        }
        Optional<Client> client = transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });

        client.ifPresent(value -> cache.put(idAsString, value));

        return client;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
        });

        cache.clear();
        for (Client client : clients) {
            cache.put(String.valueOf(client.getId()), client);
        }
        return clients;
    }
}
