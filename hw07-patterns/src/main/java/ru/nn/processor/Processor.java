package ru.nn.processor;

import ru.nn.model.Message;

@SuppressWarnings("java:S1135")
public interface Processor {

    Message process(Message message);
}
