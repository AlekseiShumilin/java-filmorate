package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    @Autowired
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) {
        if (userStorage.getUser(userId).isEmpty() || userStorage.getUser(friendId).isEmpty()) {
            throw new UserNotFoundException("Пользователи не найдены");
        } else {
            userStorage.addFriend(userId, friendId);
        }
    }

    public List<User> getFriends(Integer userId) {
        userStorage.getFriends(userId);
        if (userStorage.getUser(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден.");
        } else {
            return userStorage.getFriends(userId);
        }
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        if (userStorage.getUser(userId).isEmpty() || userStorage.getUser(friendId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден.");
        } else {
            return userStorage.getCommonFriends(userId, friendId);
        }
    }
}
