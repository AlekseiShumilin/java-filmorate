package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class UserValidationTest {
    UserController userController = new UserController();
    User userNull;
    User user0;
    User user1;
    User user2;
    User user3;
    User user4;
    User user5;
    User user6;
    User user7;

    @Test
    void createNormalUser() {
        user0 = new User("User0", "user0@mail.ru", "loginUser0",
                LocalDate.of(1987, 1, 1));
        userController.addUser(user0);
        user1 = new User("User1", "user1@mail.ru", "loginUser1",
                LocalDate.of(1987, 1, 1));
        userController.addUser(user1);
        assertEquals(user1, userController.userRepository.getUser(2));
        assertEquals(userController.userRepository.getAllUsers().size(), 2);
    }

    @Test
    void createUserWrongLogin() {
        user2 = new User("User2", "user2@mail.ru", "loginUser 2",
                LocalDate.of(1987, 1, 1));
        assertThrows(ValidationException.class, () -> userController.addUser(user2));
    }

    @Test
    void createUserBlankLogin() {
        user3 = new User("User3", "user3@mail.ru", "",
                LocalDate.of(1987, 1, 1));
        assertThrows(ValidationException.class, () -> userController.addUser(user3));
    }

    @Test
    void createUserWrongEmail() {
        user4 = new User("User4", "user4mail.ru", "loginUser4",
                LocalDate.of(1987, 1, 1));
        assertThrows(ValidationException.class, () -> userController.addUser(user4));
    }

    @Test
    void createUserBlankEmail() {
        user5 = new User("User5", "", "loginUser5",
                LocalDate.of(1987, 1, 1));
        assertThrows(ValidationException.class, () -> userController.addUser(user5));
    }

    @Test
    void createUserWrongBirthday() {
        user6 = new User("User6", "user6@mail.ru", "loginUser6",
                LocalDate.of(2024, 1, 1));
        assertThrows(ValidationException.class, () -> userController.addUser(user6));
    }

    @Test
    void createUserBlankName() {
        user7 = new User("", "user7@mail.ru", "loginUser7",
                LocalDate.of(2000, 1, 1));
        userController.addUser(user7);
        assertEquals(userController.userRepository.getUser(1), user7);
        assertEquals(userController.userRepository.getUser(1).getName(), "loginUser7");
    }

    @Test
    void createUserNull() {
        assertThrows(ValidationException.class, () -> userController.addUser(userNull));
    }

    @Test
    void updateUser() {
        User user8 = new User("User8", "user8@mail.ru", "loginUser8",
                LocalDate.of(1900, 1, 1));
        user8.setId(1);
        userController.addUser(user8);
        User user9 = new User("User9", "user9@mail.ru", "loginUser9",
                LocalDate.of(1999, 1, 1));
        user9.setId(1);
        userController.updateUser(user9);
        assertEquals(userController.userRepository.getUser(1).getName(), "User9");
        assertEquals(userController.userRepository.getUser(1).getLogin(), "loginUser9");
        assertEquals(userController.userRepository.getUser(1).getEmail(), "user9@mail.ru");
        assertEquals(userController.userRepository.getUser(1).getBirthday(),
                LocalDate.of(1999, 1, 1));
        User user10 = new User("User10", "user10@mail.ru", "loginUser10",
                LocalDate.of(1999, 1, 1));
        user9.setId(111);
        assertThrows(ValidationException.class, () -> userController.updateUser(user10));
    }

    @Test
    void getAllUsers() {
        user0 = new User("User0", "user0@mail.ru", "loginUser0",
                LocalDate.of(1987, 1, 1));
        userController.addUser(user0);
        user1 = new User("User1", "user1@mail.ru", "loginUser1",
                LocalDate.of(1987, 1, 1));
        userController.addUser(user1);
        assertEquals(userController.getAllUsers().size(), 2);
        assertTrue(userController.getAllUsers().contains(user1));
        assertTrue(userController.getAllUsers().contains(user0));
    }
}
