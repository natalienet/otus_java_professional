package ru.nn.processor.homework;

import ru.nn.model.Message;
import ru.nn.processor.Processor;

public class ProcessorSwapField11AndField12 implements Processor {
    @Override
    public Message process(Message message) {
        var field11Value = message.getField11();
        return message.toBuilder()
                .field11(message.getField12())
                .field12(field11Value)
                .build();
    }
}
