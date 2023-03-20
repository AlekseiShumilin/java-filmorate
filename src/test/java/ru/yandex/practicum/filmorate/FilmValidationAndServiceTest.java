package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.FilmValidator;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;

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

    @Test
    void addNullFilm() {
        assertThrows(ValidationException.class, () -> filmController.addFilm(filmNull));
    }

    @Test
    void addNormalFilm() {
        film0 = new Film("Film0", "Film0 description",
                LocalDate.of(2000, 10, 1), 120);
        film1 = new Film("Film1", "Film1 description",
                LocalDate.of(2000, 10, 1), 120);
        filmController.addFilm(film0);
        filmController.addFilm(film1);
        assertEquals(filmController.filmStorage.getFilm(1), film0);
        assertEquals(filmController.filmStorage.getFilm(2), film1);
        assertEquals(filmController.getAllFilms().size(), 2);
    }
    @Test
    void deleteFilm(){
        film1 = new Film("Film1", "Film1 description",
                LocalDate.of(2000, 10, 1), 120);
        filmController.addFilm(film1);
        filmStorage.deleteFilm(1);
        assertTrue(filmStorage.getAllFilms().isEmpty());

    }

    @Test
    void addFilmEmptyName() {
        film2 = new Film("", "Film2 description", LocalDate.of(2000, 10, 1), 120);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film2));
    }

    @Test
    void addFilmBlankName() {
        film3 = new Film("  ", "Film3 description",
                LocalDate.of(2000, 10, 1), 120);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film3));
    }

    @Test
    void addFilmWrongReleaseDate() {
        film4 = new Film("Film4", "Film4 description",
                LocalDate.of(1895, 12, 27), 120);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film4));
    }

    @Test
    void addFilmEarliestReleaseDate() {
        film5 = new Film("Film5", "Film5 description",
                LocalDate.of(1895, 12, 28), 120);
        filmController.addFilm(film5);
        assertEquals(filmController.filmStorage.getFilm(1), film5);
    }

    @Test
    void addFilmTooLongDescription() {
        film6 = new Film("Film6",
                "Description Description Description Description Description Description Description" +
                        " Description Description Description Description Description Description Description" +
                        " Description Description Description",
                LocalDate.of(2000, 10, 1), 120);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film6));
    }

    @Test
    void addFilmWrongDuration() {
        film7 = new Film("Film7", "Film7 description",
                LocalDate.of(2000, 10, 1), 0);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film7));
    }

    @Test
    void addFilmNegativeDuration() {
        film8 = new Film("Film8", "Film8 description",
                LocalDate.of(2000, 10, 1), -1);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film8));
    }

    @Test
    void updateFilm() {
        film0 = new Film("Film0", "Film0 description",
                LocalDate.of(2000, 10, 1), 120);
        film1 = new Film("Film1", "Film1 description",
                LocalDate.of(1999, 10, 1), 100);
        filmController.addFilm(film0);
        film1.setId(1);
        filmController.updateFilm(film1);
        assertEquals(filmController.filmStorage.getFilm(1).getName(), "Film1");
        assertEquals(filmController.filmStorage.getFilm(1).getDescription(), "Film1 description");
        assertEquals(filmController.filmStorage.getFilm(1).getDuration(), 100);
        assertEquals(filmController.filmStorage.getFilm(1).getReleaseDate(),
                LocalDate.of(1999, 10, 1));

    }

    @Test
    void likeAndUnlikeFilm() {
        film1 = new Film("Film1", "Film1 description",
                LocalDate.of(2000, 10, 1), 120);
        filmController.addFilm(film1);
        user1 = new User("User1", "user1@mail.ru", "loginUser1",
                LocalDate.of(1987, 1, 1));
        userStorage.addUser(user1);
        filmController.likeFilm(1, 1);
        assertTrue(film1.getLikes().contains((long) 1));
        filmController.unlikeFilm(1, 1);
        assertTrue(film1.getLikes().isEmpty());

    }

    @Test
    void getAllFilms() {
        film0 = new Film("Film0", "Film0 description",
                LocalDate.of(2000, 10, 1), 120);
        film1 = new Film("Film1", "Film1 description",
                LocalDate.of(2000, 10, 1), 120);
        filmController.addFilm(film1);
        filmController.addFilm(film0);
        assertTrue(filmController.filmStorage.getAllFilms().contains(film0));
        assertTrue(filmController.filmStorage.getAllFilms().contains(film1));
    }

    @Test
    void getMostPopularFilm() {
        film0 = new Film("Film0", "Film0 description",
                LocalDate.of(2000, 10, 1), 120);
        film1 = new Film("Film1", "Film1 description",
                LocalDate.of(2000, 10, 1), 120);
        film2 = new Film("Film2", "Film2 description",
                LocalDate.of(2000, 10, 1), 120);
        filmController.addFilm(film1);
        filmController.addFilm(film0);
        filmController.addFilm(film2);

        user1 = new User("User1", "user1@mail.ru", "loginUser1",
                LocalDate.of(1987, 1, 1));
        userStorage.addUser(user1);
        user2 = new User("User2", "user2@mail.ru", "loginUser2",
                LocalDate.of(1987, 1, 1));
        userStorage.addUser(user2);
        user3 = new User("User3", "user1@mail.ru", "loginUser3",
                LocalDate.of(1987, 1, 1));
        userStorage.addUser(user3);
        filmController.likeFilm(1, 1);
        filmController.likeFilm(1, 2);
        filmController.likeFilm(1, 3);

        filmController.likeFilm(2, 1);
        filmController.likeFilm(2, 2);
        filmController.likeFilm(3, 3);
        assertEquals(filmController.getMostPopularFilms(1).get(0), film1);
        assertThrows(IncorrectParameterException.class, () -> filmController.getMostPopularFilms(-1));

    }

    @Test
    void getFilmById() {
        film0 = new Film("Film0", "Film0 description",
                LocalDate.of(2000, 10, 1), 120);
        filmController.addFilm(film0);
        assertEquals(film0, filmController.getFilmById(1));
        assertThrows(FilmNotFoundException.class, () -> filmController.getFilmById(10));
    }
}
