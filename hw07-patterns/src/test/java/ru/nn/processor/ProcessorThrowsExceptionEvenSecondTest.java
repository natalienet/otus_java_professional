package ru.nn.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nn.model.Message;
import ru.nn.processor.homework.ParityOfSecondChecker;
import ru.nn.processor.homework.ProcessorThrowsExceptionEvenSecond;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProcessorThrowsExceptionEvenSecondTest {

    @Test
    @DisplayName("Тестируем вызов исключения в четную секунду")
    void throwExceptionEvenSecondTest() {
        var message = new Message.Builder(1L).field9("field9").build();
        var parityOfSecondChecker = mock(ParityOfSecondChecker.class);
        var processor = new ProcessorThrowsExceptionEvenSecond(parityOfSecondChecker);

        when(parityOfSecondChecker.isSecondEven()).thenReturn(true);

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> processor.process(message));
    }

    @Test
    @DisplayName("Тестируем возврат сообщения в нечетную секунду")
    void returnMessageOddSecondTest() {
        var message = new Message.Builder(1L).field9("field9").build();
        var parityOfSecondChecker = mock(ParityOfSecondChecker.class);
        var processor = new ProcessorThrowsExceptionEvenSecond(parityOfSecondChecker);

        when(parityOfSecondChecker.isSecondEven()).thenReturn(false);

        assertThat(processor.process(message)).isEqualTo(message);
    }
}
