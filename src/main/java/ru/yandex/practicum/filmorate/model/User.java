package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    private final String email;
    private final String login;
    private String name;

    public User(String name,String email, String login, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    private final LocalDate birthday;
}
