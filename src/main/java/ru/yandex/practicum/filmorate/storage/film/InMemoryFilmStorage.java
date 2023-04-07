package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
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
    public void likeFilm(int filmId, int userId) {
        films.get(filmId).addLike(userId);
    }

    @Override
    public void unlikeFilm(int filmId, int userId) {
        films.get(filmId).removeLike(userId);

    }

    @Override
    public void deleteFilm(int id) {
        films.remove(id);
    }

    @Override
    public void addFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.info("filmUpdate {}. Film with id " + film.getId() + "is not exist.", film);
            throw new FilmNotFoundException("Film with id " + film.getId() + "is not exist.");
        }
        return Optional.of(films.get(film.getId()));
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Optional<Film> getFilm(Integer id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Film with id " + id + " not found.");
        } else {
            return Optional.of(films.get(id));
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
