package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    void addUser(User user);

    void updateUser(User user);

    Collection<User> getAllUsers();

    User getUser(Integer id);
    void deleteUser(int id);
}
