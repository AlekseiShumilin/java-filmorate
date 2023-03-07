package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
@Component
public class UserValidator {
    public void validateUser(User user) {
        if (user == null) {
            log.info("addUser {} - user is null", user);
            throw new ValidationException("User is null.");
        } else if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            log.info("addUser {}. Incorrect login.", user);
            throw new ValidationException("Incorrect login.");
        } else if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            log.info("addUser {}. Incorrect e-mail.", user);
            throw new ValidationException("Incorrect e-mail.");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("addUser {}. Incorrect birthday date.", user);
            throw new ValidationException("Incorrect birthday date.");
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
