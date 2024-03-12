package ru.nn.dao;

import java.util.Optional;
import ru.nn.model.User;

public interface UserDao {

    Optional<User> findById(long id);

    Optional<User> findRandomUser();

    Optional<User> findByLogin(String login);
}
