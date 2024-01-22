package ru.nn.processor.homework;

import java.time.LocalTime;

public class ParityOfSecondChecker {
    public boolean isSecondEven() {
        return LocalTime.now().getSecond() % 2 == 0;
    }
}
