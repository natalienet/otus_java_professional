package ru.nn.services;

import java.lang.annotation.Annotation;
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
        List<Method> beforeMethods = getAnnotatedMethods(clazz, Before.class);
        List<Method> testMethods = getAnnotatedMethods(clazz, Test.class);
        List<Method> afterMethods = getAnnotatedMethods(clazz, After.class);
        invokeMethods(clazz, beforeMethods, testMethods, afterMethods, statistics);
        printStatistic(statistics);
    }

    private List<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        List<Method> methods = new ArrayList<>();
        Method[] methodsAll = clazz.getDeclaredMethods();
        for (Method method : methodsAll) {
            if (method.isAnnotationPresent(annotationClass)) {
                methods.add(method);
            }
        }

        return methods;
    }

    private void invokeMethods(Class<?> clazz, List<Method> beforeMethods, List<Method> testMethods,
                               List<Method> afterMethods, TestStatistics statistics)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object object;
        boolean beforeResult = false;
        for (Method testMethod : testMethods) {
            statistics.setTotalTests(statistics.getTotalTests() + 1);
            object = clazz.getConstructor().newInstance();
            for (Method method : beforeMethods) {
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

            for (Method method : afterMethods) {
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
