package ru.nn.services;

import ru.nn.annotations.After;
import ru.nn.annotations.Before;
import ru.nn.annotations.Test;

public class TestService {

    @Before
    void StartMethod() {
        System.out.println("@Before");
        System.out.println("Instance of the test class: " + Integer.toHexString(hashCode()));
    }

    @Test
    void TestMethod1() {
        System.out.println("@Test: method 1");
        System.out.println("Instance of the test class: " + Integer.toHexString(hashCode()));
    }

    @Test
    void TestMethod2() {
        System.out.println("@Test: method 2");
        System.out.println("Instance of the test class: " + Integer.toHexString(hashCode()));
        throw new AssertionError();
    }

    @Test
    void TestMethod3() {
        System.out.println("@Test: method 3");
        System.out.println("Instance of the test class: " + Integer.toHexString(hashCode()));
    }

    @Test
    void TestMethod4() {
        System.out.println("@Test: method 4");
        System.out.println("Instance of the test class: " + Integer.toHexString(hashCode()));
        throw new AssertionError();
    }

    @Test
    void TestMethod5() {
        System.out.println("@Test: method 5");
        System.out.println("Instance of the test class: " + Integer.toHexString(hashCode()));
    }

    @After
    void FinalMethod() {
        System.out.println("@After");
        System.out.println("Instance of the test class: " + Integer.toHexString(hashCode()));
    }
}
