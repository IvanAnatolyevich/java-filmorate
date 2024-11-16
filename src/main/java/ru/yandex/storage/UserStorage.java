package ru.yandex.storage;

import ru.yandex.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    User createUser(User user);

    Collection<User> allUsers();

    User updateUser(User newUser);

    User deleteUser(User user);

    Map<Long, User> getMap();
}
