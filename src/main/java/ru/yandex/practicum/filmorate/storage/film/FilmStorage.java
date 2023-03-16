package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    void addFilm(Film film);

    void updateFilm(Film film);

    Collection<Film> getAllFilms();

    Film getFilm(Integer id);
    List<Film> getMostPopularFilm(int count);


}
