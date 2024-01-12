package ru.nn.processor.homework;

import java.time.LocalTime;
import ru.nn.model.Message;
import ru.nn.processor.Processor;

public class ProcessorThrowsExceptionEvenSecond implements Processor {
    @Override
    public Message process(Message message) {
        if (LocalTime.now().getSecond() % 2 == 0) {
            throw new RuntimeException();
        }
        return message;
    }
}
