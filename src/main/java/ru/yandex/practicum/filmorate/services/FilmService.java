package ru.yandex.practicum.filmorate.services;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Optional;


@Service
public class FilmService {
    public FilmStorage filmStorage;
    public UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void likeFilm(int filmId, int userId) {
        Optional<User> user = userStorage.getUser(userId);
        if(user.isEmpty()) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        Optional<Film> film = filmStorage.getFilm(filmId);
        if(film.isEmpty()) {
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден");
        }
        filmStorage.likeFilm(filmId, userId);
    }

    public void unlikeFilm(int filmId, int userId) {
        Optional<User> user = userStorage.getUser(userId);
        if(user.isEmpty()) {
            throw new FilmNotFoundException("Пользователь с id " + userId + " не найден");
        }
        Optional<Film> film = filmStorage.getFilm(filmId);
        if(film.isEmpty()) {
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден");
        }
        filmStorage.unlikeFilm(filmId, userId);
    }
}
