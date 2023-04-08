package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.services.UserValidator;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserStorage userStorage;
    private final UserValidator userValidator;
    private final UserService userService;

    public UserController(UserStorage userStorage, UserValidator userValidator, UserService userService) {
        this.userService = userService;
        this.userStorage = userStorage;
        this.userValidator = userValidator;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("addUser {}.", user);
        userValidator.validateUser(user);
        userStorage.addUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("updateUser {}.", user);
        userStorage.updateUser(user);
        Optional<User> updatedUser = userStorage.getUser(user.getId());
        if (updatedUser.isEmpty()) {
            throw new UserNotFoundException("Пользователь с id " + user.getId() + " не найден");
        } else {
            return updatedUser.get();
        }
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("getAllUsers.");
        return userStorage.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        log.info("get user {}.", userId);
        Optional<User> user = userStorage.getUser(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден.");
        } else {
            return user.get();
        }
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable Integer userId) {
        log.info("get user's friends {}.", userId);
        return userService.getFriends(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("user {} add friend user {}.", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("user {} remove friend user {}.", id, friendId);
        userStorage.deleteFriend(id, friendId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int userId, @PathVariable int otherId) {
        log.info("user {} get common friends with other user {}.", userId, otherId);
        return userService.getCommonFriends(userId, otherId);
    }
}
