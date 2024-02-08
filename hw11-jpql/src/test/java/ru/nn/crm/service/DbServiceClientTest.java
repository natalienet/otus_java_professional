package ru.nn.crm.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nn.crm.model.Address;
import ru.nn.crm.model.Client;
import ru.nn.crm.model.Phone;
import ru.nn.base.AbstractHibernateTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Демо работы с hibernate (с абстракциями) должно ")
class DbServiceClientTest extends AbstractHibernateTest {

    @Test
    @DisplayName(" корректно сохранять, изменять и загружать клиента")
    void shouldCorrectSaveClient() {
        var client = new Client(null, "Vasya", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"),
                new Phone(null, "14-666-333")));

        var savedClient = dbServiceClient.saveClient(client);
        System.out.println(savedClient);

        var loadedSavedClient = dbServiceClient.getClient(savedClient.getId());
        assertThat(loadedSavedClient)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(savedClient);

        var savedClientUpdated = loadedSavedClient.get();
        savedClientUpdated.setName("updatedName");
        dbServiceClient.saveClient(savedClientUpdated);

        var loadedClient = dbServiceClient.getClient(savedClientUpdated.getId());
        assertThat(loadedClient).isPresent().get().usingRecursiveComparison().isEqualTo(savedClientUpdated);
        System.out.println(loadedClient);

        var clientList = dbServiceClient.findAll();

        assertThat(clientList).hasSize(1);
        assertThat(clientList.get(0)).usingRecursiveComparison().isEqualTo(loadedClient.get());
    }
}
