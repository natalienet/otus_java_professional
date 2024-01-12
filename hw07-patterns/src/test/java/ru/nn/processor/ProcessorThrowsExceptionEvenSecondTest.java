package ru.nn.processor;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nn.model.Message;
import ru.nn.processor.homework.ProcessorThrowsExceptionEvenSecond;

class ProcessorThrowsExceptionEvenSecondTest {
    @Test
    @DisplayName("Тестируем вызов исключения в четную секунду")
    void throwExceptionEvenSecondTest() {
        var message = new Message.Builder(1L).field9("field9").build();

        var processor = mock(ProcessorThrowsExceptionEvenSecond.class);
        when(processor.process(message)).thenThrow(new RuntimeException());

        if (LocalTime.now().getSecond() % 2 != 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> processor.process(message));
    }

    @Test
    @DisplayName("Тестируем возврат сообщения в нечетную секунду")
    void returnMessageOddSecondTest() {
        var message = new Message.Builder(1L).field9("field9").build();

        var processor = mock(ProcessorThrowsExceptionEvenSecond.class);
        when(processor.process(message)).thenReturn(message);

        if (LocalTime.now().getSecond() % 2 == 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        assertThat(processor.process(message)).isEqualTo(message);
    }
}
