package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpa {
    String name;
    Integer id;

    public Mpa(String name, int id) {
        this.name = name;
        this.id = id;
    }

}
