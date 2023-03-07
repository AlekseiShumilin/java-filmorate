package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repositories.FilmRepository;
import ru.yandex.practicum.filmorate.services.FilmValidator;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    public FilmRepository filmRepository = new FilmRepository();
    public FilmValidator filmValidator = new FilmValidator();

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("addFilm {}.", film);
        filmValidator.validateFilm(film);
        filmRepository.addFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("updateFilm {}.", film);
       filmRepository.updateFilm(film);
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("getAllFilms.");
        return filmRepository.getAllFilms();
    }
}
