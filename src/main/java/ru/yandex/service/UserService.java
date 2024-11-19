package ru.yandex.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.storage.FriendshipRepository;
import ru.yandex.storage.UserRepository;
import ru.yandex.exception.DuplicateKeyException;
import ru.yandex.exception.NotFoundException;
import ru.yandex.exception.ValidationException;
import ru.yandex.model.User;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    public User addUser(User user) {
        try {
            return userRepository.add(user);
        } catch (DuplicateKeyException e) {
            throw new ValidationException("Email already exists: " + user.getEmail());
        }
    }

    public Optional<User> updateUser(User user) {
        userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%s не найден",
                        user.getId())));
        return Optional.ofNullable(userRepository.update(user));
    }

    public void addFriend(long userId, long friendId) {
        log.info("Добавление друга {} к пользователю {}", friendId, userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%s не найден", userId)));
        userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%s не найден", friendId)));

        if (friendshipRepository.isFriend(userId, friendId)) {
            throw new DuplicateKeyException("Друг уже добавлен");
        }

        friendshipRepository.addFriend(userId, friendId);
        log.info("Пользователь {} добавлен в друзья пользователю {}", friendId, userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%s не найден", userId)));
        userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%s не найден", friendId)));
        friendshipRepository.removeFriend(userId, friendId);
    }

    public List<User> getCommonFriends(Long userId1, Long userId2) {
        return friendshipRepository.getCommonFriends(userId1, userId2);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%s не найден", id))));
    }

    public List<User> getUserFriends(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%s не найден", userId)));
        return friendshipRepository.getUserFriends(userId);
    }
}

