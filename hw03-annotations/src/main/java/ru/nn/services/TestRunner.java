package ru.nn.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import ru.nn.annotations.After;
import ru.nn.annotations.Before;
import ru.nn.annotations.Test;
import ru.nn.domain.TestStatistics;

public class TestRunner {
    public void run(String className)
            throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException,
                    IllegalAccessException {

        Class<?> clazz = Class.forName(className);
        TestStatistics statistics = new TestStatistics();
        List<List<Method>> methods = getAnnotatedMethods(clazz);
        invokeMethods(clazz, methods, statistics);
        printStatistic(statistics);
    }

    private List<List<Method>> getAnnotatedMethods(Class<?> clazz) {
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();
        Method[] methodsAll = clazz.getDeclaredMethods();
        for (Method method : methodsAll) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            }

            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }

            if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
        }
        List<List<Method>> methods = new ArrayList<>();
        methods.add(beforeMethods);
        methods.add(testMethods);
        methods.add(afterMethods);
        return methods;
    }

    private void invokeMethods(Class<?> clazz, List<List<Method>> methods, TestStatistics statistics)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object object;
        List<Method> testMethods = methods.get(1);
        boolean beforeResult = false;
        for (Method testMethod : testMethods) {
            statistics.setTotalTests(statistics.getTotalTests() + 1);
            object = clazz.getConstructor().newInstance();
            for (Method method : methods.get(0)) {
                beforeResult = invokeMethod(object, method);
                if (!beforeResult) {
                    break;
                }
            }

            if (beforeResult) {
                if (invokeMethod(object, testMethod)) {
                    statistics.setSuccessfulTests(statistics.getSuccessfulTests() + 1);
                } else {
                    statistics.setFailedTests(statistics.getFailedTests() + 1);
                }
            }

            for (Method method : methods.get(2)) {
                invokeMethod(object, method);
            }
        }
    }

    private boolean invokeMethod(Object object, Method method) {
        try {
            method.setAccessible(true);
            method.invoke(object);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void printStatistic(TestStatistics statistics) {
        System.out.println("-----------STATISTICS-----------");
        System.out.printf("Total tests: %d%n", statistics.getTotalTests());
        System.out.printf("Successful tests: %d%n", statistics.getSuccessfulTests());
        System.out.printf("Failed tests: %d%n", statistics.getFailedTests());
    }
}
