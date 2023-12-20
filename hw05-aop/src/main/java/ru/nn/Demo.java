package ru.nn;

public class Demo {
    public static void main(String[] args) {
        new Demo().action();
    }

    public void action() {
        TestLoggingInterface testLoggingClass = Ioc.createTestClass();
        testLoggingClass.calculation(1);
        testLoggingClass.calculation(1, 2);
        testLoggingClass.calculation(1, 2, "123");
    }
}
