package ru.nn;

import java.lang.reflect.InvocationTargetException;
import ru.nn.services.TestRunner;

public class Main {
    public static void main(String[] args)
            throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException,
                    IllegalAccessException {
        TestRunner runner = new TestRunner();
        runner.run("ru.nn.services.TestService");
    }
}
