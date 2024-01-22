package ru.nn;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nn.handler.ComplexProcessor;
import ru.nn.listener.homework.HistoryListener;
import ru.nn.model.Message;
import ru.nn.model.ObjectForMessage;
import ru.nn.processor.homework.ParityOfSecondChecker;
import ru.nn.processor.homework.ProcessorSwapField11AndField12;
import ru.nn.processor.homework.ProcessorThrowsExceptionEvenSecond;

public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        var processors = List.of(new ProcessorSwapField11AndField12(),
                new ProcessorThrowsExceptionEvenSecond(new ParityOfSecondChecker()));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        var data = "123";
        var field13 = new ObjectForMessage();
        var field13Data = new ArrayList<String>();
        field13Data.add(data);
        field13.setData(field13Data);
        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .build();

        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);

        complexProcessor.removeListener(historyListener);
    }
}
