package ru.nn.processor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nn.model.Message;
import ru.nn.processor.homework.ProcessorThrowsExceptionEvenSecond;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class ProcessorThrowsExceptionEvenSecondTest {
    static Message message;
    static ProcessorThrowsExceptionEvenSecond processor;

    @BeforeAll
    static void setUp() {
        message = new Message.Builder(1L).field9("field9").build();
        processor = new ProcessorThrowsExceptionEvenSecond();
    }

    @Test
    @DisplayName("Тестируем вызов исключения в четную секунду")
    void throwExceptionEvenSecondTest() {
        int seconds;
        LocalTime time = LocalTime.now();

        while (LocalTime.now().minusSeconds(5).compareTo(time) < 0) {
            try {
                processor.process(message);
            } catch (RuntimeException e) {
                seconds = LocalTime.now().getSecond();
                assertThat(seconds % 2).isEqualTo(0);
                return;
            }

        }
        fail();
    }

    @Test
    @DisplayName("Тестируем возврат сообщения в нечетную секунду")
    void returnMessageOddSecondTest() {
        Message returnedMessage;
        LocalTime time = LocalTime.now();

        while (LocalTime.now().minusSeconds(5).compareTo(time) < 0) {
            try {
                returnedMessage = processor.process(message);
                assertThat(LocalTime.now().getSecond() % 2).isEqualTo(1);
                assertThat(returnedMessage).isEqualTo(message);
                return;
            } catch (RuntimeException e) {
            }
        }
    }
}
