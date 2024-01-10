package ru.nn.services;

import ru.nn.api.OutputService;

public class ConsoleOutputService implements OutputService {

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }
}
