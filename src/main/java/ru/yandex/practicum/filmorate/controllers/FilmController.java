package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.services.FilmValidator;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    public FilmStorage filmStorage;
    public FilmValidator filmValidator;
    public FilmService filmService;

    public FilmController(FilmStorage filmStorage, FilmValidator filmValidator, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmValidator = filmValidator;
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("addFilm {}.", film);
        filmValidator.validateFilm(film);
        filmStorage.addFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("updateFilm {}.", film);
        Optional<Film> filmUpdated = filmStorage.updateFilm(film);
        if (filmUpdated.isEmpty()) {
            throw new FilmNotFoundException("Фильм с id " + film.getId() + " не найден.");
        } else {
            return filmUpdated.get();
        }
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("getAllFilms.");
        return filmStorage.getAllFilms();
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void likeFilm(@PathVariable int filmId, @PathVariable int userId) {
        log.info("user {} likes film {}.", userId, filmId);
        filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void unlikeFilm(@PathVariable int filmId, @PathVariable int userId) {
        log.info("user {} unlikes film {}.", userId, filmId);
        filmService.unlikeFilm(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10", required = false) int count) {
        log.info("get {} most popular films", count);
        if (count <= 0) {
            throw new IncorrectParameterException("count", "Parameter count should be higher than 0.");
        } else {
            return filmStorage.getMostPopularFilm(count);
        }
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        log.info("get film {}.", filmId);
        Optional<Film> film = filmStorage.getFilm(filmId);
        if (film.isEmpty()) {
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден.");
        }
        return film.get();

    }

}
