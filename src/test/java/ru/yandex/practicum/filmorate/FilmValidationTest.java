package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;

class FilmValidationTest {
    FilmController filmController = new FilmController();
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
        assertEquals(filmController.filmRepository.getFilm(1), film0);
        assertEquals(filmController.filmRepository.getFilm(2), film1);
        assertEquals(filmController.getAllFilms().size(), 2);
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
        assertEquals(filmController.filmRepository.getFilm(1), film5);
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
        assertEquals(filmController.filmRepository.getFilm(1).getName(), "Film1");
        assertEquals(filmController.filmRepository.getFilm(1).getDescription(), "Film1 description");
        assertEquals(filmController.filmRepository.getFilm(1).getDuration(), 100);
        assertEquals(filmController.filmRepository.getFilm(1).getReleaseDate(),
                LocalDate.of(1999, 10, 1));

    }

    @Test
    void getAllFilms() {
        film0 = new Film("Film0", "Film0 description",
                LocalDate.of(2000, 10, 1), 120);
        film1 = new Film("Film1", "Film1 description",
                LocalDate.of(2000, 10, 1), 120);
        filmController.addFilm(film1);
        filmController.addFilm(film0);
        assertTrue(filmController.filmRepository.getAllFilms().contains(film0));
        assertTrue(filmController.filmRepository.getAllFilms().contains(film1));
    }
}
