package ru.nn.processor.homework;

import ru.nn.model.Message;
import ru.nn.processor.Processor;

public class ProcessorThrowsExceptionEvenSecond implements Processor {
    private final ParityOfSecondChecker parityOfSecondChecker;

    public ProcessorThrowsExceptionEvenSecond(ParityOfSecondChecker parityOfSecondChecker) {
        this.parityOfSecondChecker = parityOfSecondChecker;
    }

    @Override
    public Message process(Message message) {
        if (parityOfSecondChecker.isSecondEven()) {
            throw new RuntimeException();
        }
        return message;
    }
}
