package ru.nn.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nn.model.Message;

public class LoggerProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(LoggerProcessor.class);

    private final Processor processor;

    public LoggerProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public Message process(Message message) {
        logger.info("log processing message:{}", message);
        return processor.process(message);
    }
}
