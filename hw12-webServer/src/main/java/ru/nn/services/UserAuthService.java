package ru.nn.services;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
