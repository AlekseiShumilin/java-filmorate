package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.FilmValidator;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationAndServiceTest {
    FilmStorage filmStorage = new InMemoryFilmStorage();
    UserStorage userStorage = new InMemoryUserStorage();
    FilmService filmService = new FilmService(filmStorage, userStorage);
    FilmValidator filmValidator = new FilmValidator();
    FilmController filmController = new FilmController(filmStorage, filmValidator, filmService);
    Film filmNull;
    Film film0;
    Film film1;
    Film film2;
    Film film3;
    Film film4;
    Film film5;
    Film film6;
    Film film7;
    Film film8;
    User user1;
    User user2;
    User user3;

    void initialize() {
        film0 = new Film("Film0", "Film0 description",
                LocalDate.of(2000, 10, 1),
                120, new Mpa("G", 1));
        film1 = new Film("Film1", "Film1 description",
                LocalDate.of(2000, 10, 1), 120, new Mpa("G", 1));
        film2 = new Film("Film2", "Film2 description",
                LocalDate.of(2000, 10, 1), 120, new Mpa("G", 1));
    }

    @Test
    void addNullFilm() {
        assertThrows(ValidationException.class, () -> filmController.addFilm(filmNull));
    }

    @Test
    void addNormalFilm() {
        initialize();
        filmController.addFilm(film0);
        filmController.addFilm(film1);
        assertEquals(filmController.filmStorage.getFilm(1), Optional.of(film0));
        assertEquals(filmController.filmStorage.getFilm(2), Optional.of(film1));
        assertEquals(filmController.getAllFilms().size(), 2);
    }

    @Test
    void deleteFilm() {
        initialize();
        filmController.addFilm(film1);
        filmStorage.deleteFilm(1);
        assertTrue(filmStorage.getAllFilms().isEmpty());

    }

    @Test
    void addFilmEmptyName() {
        film2 = new Film("", "Film2 description",
                LocalDate.of(2000, 10, 1), 120, new Mpa("G", 1));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film2));
    }

    @Test
    void addFilmBlankName() {
        film3 = new Film("  ", "Film3 description",
                LocalDate.of(2000, 10, 1), 120, new Mpa("G", 1));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film3));
    }

    @Test
    void addFilmWrongReleaseDate() {
        film4 = new Film("Film4", "Film4 description",
                LocalDate.of(1895, 12, 27), 120, new Mpa("G", 1));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film4));
    }

    @Test
    void addFilmEarliestReleaseDate() {
        film5 = new Film("Film5", "Film5 description",
                LocalDate.of(1895, 12, 28), 120, new Mpa("G", 1));
        filmController.addFilm(film5);
        assertEquals(filmController.filmStorage.getFilm(1), Optional.of(film5));
    }

    @Test
    void addFilmTooLongDescription() {
        film6 = new Film("Film6",
                "Description Description Description Description Description Description Description" +
                        " Description Description Description Description Description Description Description" +
                        " Description Description Description",
                LocalDate.of(2000, 10, 1), 120, new Mpa("G", 1));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film6));
    }

    @Test
    void addFilmWrongDuration() {
        film7 = new Film("Film7", "Film7 description",
                LocalDate.of(2000, 10, 1), 0, new Mpa("G", 1));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film7));
    }

    @Test
    void addFilmNegativeDuration() {
        film8 = new Film("Film8", "Film8 description",
                LocalDate.of(2000, 10, 1), -1, new Mpa("G", 1));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film8));
    }

    @Test
    void updateFilm() {
        initialize();
        filmController.addFilm(film0);
        film1.setId(1);
        filmController.updateFilm(film1);
        assertEquals(filmController.filmStorage.getFilm(1).get().getName(), "Film1");
        assertEquals(filmController.filmStorage.getFilm(1).get().getDescription(), "Film1 description");
        assertEquals(filmController.filmStorage.getFilm(1).get().getDuration(), 120);
        assertEquals(filmController.filmStorage.getFilm(1).get().getReleaseDate(),
                LocalDate.of(2000, 10, 1));

    }


    @Test
    void getAllFilms() {
        initialize();
        filmController.addFilm(film1);
        filmController.addFilm(film0);
        assertTrue(filmController.filmStorage.getAllFilms().contains(film0));
        assertTrue(filmController.filmStorage.getAllFilms().contains(film1));
    }

    @Test
    void getFilmById() {
        initialize();
        filmController.addFilm(film0);
        assertEquals(film0, filmController.getFilmById(1));
        assertThrows(FilmNotFoundException.class, () -> filmController.getFilmById(10));
    }
}
