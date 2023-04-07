package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.FilmValidator;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
        filmStorage.updateFilm(film);
        return film;
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
        if (count <= 0) {
            throw new IncorrectParameterException("count", "Parameter count should be higher than 0.");
        } else {
            return filmService.getMostPopularFilms(count);
        }
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        log.info("get film {}.", filmId);
        return filmStorage.getFilm(filmId);

    }
}
