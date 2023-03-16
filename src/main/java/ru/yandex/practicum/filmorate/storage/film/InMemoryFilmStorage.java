package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    int filmId = 0;

    public Integer generateId() {
        return ++filmId;
    }

    @Override
    public void addFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
    }

    @Override
    public void updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.info("filmUpdate {}. Film with id " + film.getId() + "is not exist.", film);
            throw new FilmNotFoundException("Film with id " + film.getId() + "is not exist.");
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film getFilm(Integer id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Film with id " + id + " not found.");
        } else {
            return films.get(id);
        }
    }

    @Override
    public List<Film> getMostPopularFilm(int count) {
        return films.values().stream().sorted((f0, f1) -> f1.getLikes().size() - f0.getLikes()
                .size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
