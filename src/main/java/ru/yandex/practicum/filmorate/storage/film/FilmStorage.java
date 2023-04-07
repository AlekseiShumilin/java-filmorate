package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    void addFilm(Film film);

    Optional<Film> updateFilm(Film film);

    Collection<Film> getAllFilms();

    Optional<Film> getFilm(Integer id);

    List<Film> getMostPopularFilm(int count);

    void deleteFilm(int id);

    void likeFilm(int filmId, int userId);

    void unlikeFilm(int filmId, int userId);

}
