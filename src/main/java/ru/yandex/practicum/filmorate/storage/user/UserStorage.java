package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    void addUser(User user);

    Optional<User> updateUser(User user);

    Collection<User> getAllUsers();

    Optional<User> getUser(Integer id);

    void deleteUser(int id);

    void addFriend(int user, int friend);

    void deleteFriend(int userId, int friendId);

    List<User> getFriends(int userId);
}
