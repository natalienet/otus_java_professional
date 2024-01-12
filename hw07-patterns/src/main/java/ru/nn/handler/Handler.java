package ru.nn.handler;

import ru.nn.listener.Listener;
import ru.nn.model.Message;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);

    void removeListener(Listener listener);
}
