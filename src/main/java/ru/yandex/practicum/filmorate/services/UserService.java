package ru.yandex.practicum.filmorate.services;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoFriendsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Integer userId, Integer friendId) {
        User friend = userStorage.getUser(friendId);
        userStorage.getUser(userId).addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFriend(int userId, int friendId) {
        User friend = userStorage.getUser(friendId);
        userStorage.getUser(userId).removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public User getUserById(Integer userId) {
        return userStorage.getUser(userId);
    }

    public List<User> getFriends(Integer userId) {
        User user = userStorage.getUser(userId);
        if (user.getFriends().isEmpty()) {
            throw new NoFriendsException("У пользователя " + userId + " нет друзей :(");
        } else {
            return user.getFriends().stream()
                    .map(userStorage::getUser)
                    .collect(Collectors.toList());
        }
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {

        return userStorage.getUser(userId).getFriends().stream()
                .filter(userStorage.getUser(friendId).getFriends()::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }
}
