package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
@Component
public class FilmValidator {
    public void validateFilm(Film film) {
        if (film == null) {
            log.info("addFilm {}. Film is null", film);
            throw new ValidationException("Film is null");
        } else if (film.getName() == null || film.getName().isBlank()) {
            log.info("addFilm {}. Film's name is blank.", film);
            throw new ValidationException("Film's name is blank.");
        } else if (film.getDescription().length() > 200) {
            log.info("addFilm {}. Description is longer than 200 characters.", film);
            throw new ValidationException("Description is longer than 200 characters.");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("addFilm {}. Release date is earlier than 28-12-1985.", film);
            throw new ValidationException("Release date is earlier than 28-12-1985.");
        } else if (film.getDuration() <= 0) {
            log.info("addFilm {}. Film's duration should be positive.", film);
            throw new ValidationException("Film's duration should be positive.");
        }
    }
}
