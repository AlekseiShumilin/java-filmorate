package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repositories.UserRepository;
import ru.yandex.practicum.filmorate.services.UserValidator;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    public UserRepository userRepository = new UserRepository();
    public UserValidator userValidator = new UserValidator();

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("addUser {}.", user);
        userValidator.validateUser(user);
        userRepository.addUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("updateUser {}.", user);
        userRepository.updateUser(user);
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("getAllUsers.");
        return userRepository.getAllUsers();
    }
}
