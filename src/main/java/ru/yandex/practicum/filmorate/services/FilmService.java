package ru.yandex.practicum.filmorate.services;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FilmService {
    public FilmStorage filmStorage;
    public UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void likeFilm(int filmId, int userId) {
        if (userStorage.getUser(userId) != null) {
            filmStorage.getFilm(filmId).addLike(userId);
        } else {
            throw new UserNotFoundException("User with id " + userId + " is not exist.");
        }
    }

    public void unlikeFilm(int filmId, int userId) {
        if (userStorage.getUser(userId) != null) {
            filmStorage.getFilm(filmId).removeLike(userId);
        } else {
            throw new UserNotFoundException("User with id " + userId + " is not exist.");
        }
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmStorage.getMostPopularFilm(count);
    }
}
