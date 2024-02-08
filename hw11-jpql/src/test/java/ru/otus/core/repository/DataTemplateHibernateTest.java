package ru.otus.core.repository;

import org.junit.jupiter.api.Test;
import ru.nn.crm.model.Address;
import ru.nn.crm.model.Client;
import ru.nn.crm.model.Phone;
import ru.otus.base.AbstractHibernateTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class DataTemplateHibernateTest extends AbstractHibernateTest {

    @Test
    void shouldSaveAndFindCorrectClientById() {
        var client = new Client(null, "Vasya", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"),
                new Phone(null, "14-666-333")));

        var savedClient = transactionManager.doInTransaction(session -> {
            clientTemplate.insert(session, client);
            return client;
        });

        assertThat(savedClient.getId()).isNotNull();
        assertThat(savedClient.getName()).isEqualTo(client.getName());

        var loadedSavedClient = transactionManager.doInReadOnlyTransaction(
                session -> clientTemplate.findById(session, savedClient.getId()).map(Client::clone));

        assertThat(loadedSavedClient)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(savedClient);

        savedClient.setName("updatedName");
        transactionManager.doInTransaction(session -> {
            clientTemplate.update(session, savedClient);
            return null;
        });

        Optional<Client> loadedClient = transactionManager.doInReadOnlyTransaction(
                session -> clientTemplate.findById(session, savedClient.getId()).map(Client::clone));
        assertThat(loadedClient).isPresent();
        assertThat(loadedClient).get().usingRecursiveComparison().isEqualTo(savedClient);

        var clientList = transactionManager.doInReadOnlyTransaction(session ->
                clientTemplate.findAll(session).stream().map(Client::clone).collect(Collectors.toList()));

        assertThat(clientList).hasSize(1);
        assertThat(clientList.get(0)).usingRecursiveComparison().isEqualTo(savedClient);

        clientList = transactionManager.doInReadOnlyTransaction(
                session -> clientTemplate.findByEntityField(session, "name", "updatedName").stream()
                        .map(Client::clone)
                        .collect(Collectors.toList()));

        assertThat(clientList).hasSize(1);
        assertThat(clientList.get(0)).usingRecursiveComparison().isEqualTo(savedClient);
    }
}
