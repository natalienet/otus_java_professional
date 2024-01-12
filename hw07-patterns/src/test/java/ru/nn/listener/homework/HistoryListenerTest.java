package ru.nn.listener.homework;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import ru.nn.model.Message;
import ru.nn.model.ObjectForMessage;

class HistoryListenerTest {

    @Test
    void listenerTest() {
        var historyListener = new HistoryListener();

        var id = 100L;
        var data = "33";
        var field13 = new ObjectForMessage();
        var field13Data = new ArrayList<String>();
        field13Data.add(data);
        field13.setData(field13Data);

        var message =
                new Message.Builder(id).field10("field10").field13(field13).build();

        historyListener.onUpdated(message);
        message.getField13().setData(new ArrayList<>());
        field13Data.clear();

        var messageFromHistory = historyListener.findMessageById(id);
        assertThat(messageFromHistory).isPresent();
        assertThat(messageFromHistory.get().getField13().getData()).containsExactly(data);
    }
}
