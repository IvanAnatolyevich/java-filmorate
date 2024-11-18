package ru.yandex.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.exception.NotFoundException;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import ru.yandex.model.User;
import ru.yandex.storage.UserStorage;

@Service
@RequiredArgsConstructor
@Getter
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    public User createUser(User user) {
       return userStorage.createUser(user);
    }

    public User deleteUser(User user) {
       return userStorage.deleteUser(user);
    }

    public User updateUser(User newUser) {
       return userStorage.updateUser(newUser);
    }

    public Collection<User> allUsers() {
       return userStorage.allUsers();
    }

    public User addFriend(Long id, Long friendId) {
        if (userStorage.getMap().containsKey(id)) {
            if (userStorage.getMap().containsKey(friendId)) {
                User user = userStorage.getMap().get(id);
                User friend = userStorage.getMap().get(friendId);
                user.getFriends().add(friendId);
                friend.getFriends().add(id);
                return userStorage.getMap().get(friendId);
            }
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        throw new NotFoundException("Пользователь с id = " + id + " не найден");
    }

    public User deleteFriend(Long id, Long friendId) {
        if (userStorage.getMap().containsKey(id)) {
            if (userStorage.getMap().containsKey(friendId)) {
                if (userStorage.getMap().get(id).getFriends().contains(friendId) &&
                        userStorage.getMap().get(friendId).getFriends().contains(id)) {
                    User user = userStorage.getMap().get(id);
                    User friend = userStorage.getMap().get(friendId);
                    user.getFriends().remove(friendId);
                    friend.getFriends().remove(id);
                    return userStorage.getMap().get(friendId);
                }
                throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
            }
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
    }

    public Collection<Long> allFriends(Long id) {
        return userStorage.getMap().get(id).getFriends();
    }

    public Collection<Long> generalFriends(Long id, Long otherId) {
        Set<Long> friends = userStorage.getMap().get(id).getFriends().stream()
                .filter(x -> (userStorage.getMap().get(otherId).getFriends()
                        .stream()
                        .anyMatch(y -> (y == x))
                ))
                .collect(Collectors.toSet());
        return friends;
    }

    public User getUserId(Long id) {
        return userStorage.getUserId(id);
    }
}
