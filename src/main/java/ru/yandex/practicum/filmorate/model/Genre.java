package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre {
    String name;
    int id;

    public Genre(String name, int id) {
        this.name = name;
        this.id = id;
    }

}
