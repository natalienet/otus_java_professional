package ru.nn;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nn.handler.ComplexProcessor;
import ru.nn.listener.ListenerPrinterConsole;
import ru.nn.model.Message;
import ru.nn.processor.LoggerProcessor;
import ru.nn.processor.ProcessorConcatFields;
import ru.nn.processor.ProcessorUpperField10;

public class Demo {
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        var processors = List.of(new ProcessorConcatFields(), new LoggerProcessor(new ProcessorUpperField10()));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .build();

        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
