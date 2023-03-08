package ru.yandex.practicum.filmorate.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class FilmRepository {
    private final Map<Integer, Film> films = new HashMap<>();
    int filmId = 0;

    public Integer generateId() {
        return ++filmId;
    }

    public void addFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
    }

    public void updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.info("filmUpdate {}. Film with id " + film.getId() + "is not exist.", film);
            throw new ValidationException("Film with id " + film.getId() + "is not exist.");
        }
    }

    public Collection<Film> getAllFilms() {
        return films.values();
    }

    public Film getFilm(Integer id) {
        return films.get(id);
    }

}
