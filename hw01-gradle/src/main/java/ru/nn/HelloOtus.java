package ru.nn;

import com.google.common.base.CharMatcher;

@SuppressWarnings("java:S106")
public class HelloOtus {
    public static void main(String[] args) {
        String input = "Hello Otus!!!";
        String out = CharMatcher.is('!').trimTrailingFrom(input);
        System.out.println(out);
    }
}
