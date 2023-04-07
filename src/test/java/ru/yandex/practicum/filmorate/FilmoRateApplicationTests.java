package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;
    final UserService userService;
    final FilmService filmService;
    User user1;
    User user2;
    User userUpdate;
    User user4;
    Film film1;
    Film film2;
    Film film3;
    Film film4;

    public void userInit() {
        user1 = new User("user_1_name", "user_1@mail.ru",
                "user_1_login", LocalDate.of(1981, 4, 25));
        userStorage.addUser(user1);
        user2 = new User("user_2_name", "user_2@mail.ru",
                "user_2_login", LocalDate.of(1982, 4, 25));
        userStorage.addUser(user2);
        userUpdate = new User("user_Update_name", "user_Update@mail.ru",
                "user_Update_login", LocalDate.of(1983, 4, 25));
        user4 = new User("user_4_name", "user_4@mail.ru",
                "user_4_login", LocalDate.of(1984, 4, 25));
    }

    @Test
    public void getMpaById() {
        Mpa mpa1 = new Mpa("G", 1);
        Assertions.assertEquals(mpaStorage.getMpaById(1), mpa1);
        Assertions.assertThrows(MpaNotFoundException.class, ()->mpaStorage.getMpaById(10));
    }

    @Test
    public void getAllMpa() {
        Assertions.assertEquals(mpaStorage.getAllMpa().size(), 5);
    }

    @Test
    public void getGenreById() {
        Genre genre = new Genre("Комедия", 1);
        Assertions.assertEquals(genreStorage.getGenreById(1), genre);
        Assertions.assertThrows(GenreNotFoundException.class, ()->genreStorage.getGenreById(10));

    }

    @Test
    public void getAllGenres() {
        Assertions.assertEquals(genreStorage.getAllGenres().size(), 6);
    }

    @Test
    public void testAddFilm() {
        film1 = new Film("Начало", "Фильм с Ди Каприо",
                LocalDate.of(2000, 1, 1),
                120, new Mpa("G", 1));
        filmStorage.addFilm(film1);
        Optional<Film> filmOptional = filmStorage.getFilm(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Начало"))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "Фильм с Ди Каприо"))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                                LocalDate.of(2000, 1, 1)))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("mpa", new Mpa("G", 1)))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 120)
                );

    }

    @Test
    public void testFindFilmById() {

        Optional<Film> filmOptional = filmStorage.getFilm(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Начало"))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "Фильм с Ди Каприо"))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                                LocalDate.of(2000, 1, 1)))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("mpa", new Mpa("G", 1)))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 120)
                );
    }

    @Test
    public void testFindFilmByFakeId() {
        Optional<Film> filmOptional = filmStorage.getFilm(10);
        Assertions.assertTrue(filmOptional.isEmpty());
    }

    @Test
    public void testGetAllFilms() {
        Film newFilm = new Film("Криминальное чтиво", "Фильм про гангстеров",
                LocalDate.of(1995, 1, 1),
                120, new Mpa("NC-17", 5));
        filmStorage.addFilm(newFilm);
        Assertions.assertEquals(filmStorage.getAllFilms().size(), 2);
    }

    @Test
    public void testUpdateFilm() {
        film2 = new Film("Криминальное чтиво-2", "Фильм про гангстеров",
                LocalDate.of(1995, 1, 1),
                130, new Mpa("R", 4));
        film2.setId(3);
        filmStorage.addFilm(film2);

        film3 = new Film("Криминальное чтиво-3", "Фильм про гангстеров",
                LocalDate.of(1995, 1, 1),
                130, new Mpa("R", 4));
        film3.setGenres(List.of(new Genre("Комедия", 1)));
        film3.setId(3);

        filmStorage.updateFilm(film3);

        Optional<Film> updatedFilm = filmStorage.getFilm(3);
        assertThat(updatedFilm)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("id", 3))
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("name", "Криминальное чтиво-3"))
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("description", "Фильм про гангстеров"))
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("releaseDate",
                                LocalDate.of(1995, 1, 1)))
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("mpa", new Mpa("R", 4)))
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("duration", 130))
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("genres",
                                List.of(new Genre("Комедия", 1)))
                );

    }

    @Test
    public void testUpdateFilmFakeId() {

        Film newFilm = new Film("Большой куш-2", "Фильм про гангстеров",
                LocalDate.of(1995, 1, 1),
                130, new Mpa("R", 4));
        newFilm.setGenres(List.of(new Genre("Комедия", 1)));
        newFilm.setId(100);

        Assertions.assertEquals(filmStorage.updateFilm(newFilm), Optional.empty());

    }

    @Test
    public void getUser() {
        userInit();
        user4.setId(4);
        userStorage.addUser(user4);

        Optional<User> user = userStorage.getUser(user4.getId());
        assertThat(user).isPresent()
                .hasValueSatisfying(u -> assertThat(u)
                        .hasFieldOrPropertyWithValue("email", "user_4@mail.ru"))
                .hasValueSatisfying(u -> assertThat(u)
                        .hasFieldOrPropertyWithValue("login", "user_4_login"))
                .hasValueSatisfying(u -> assertThat(u)
                        .hasFieldOrPropertyWithValue("name", "user_4_name"))
                .hasValueSatisfying(u -> assertThat(u)
                        .hasFieldOrPropertyWithValue("birthday",
                                LocalDate.of(1984, 4, 25)));
    }

    @Test
    public void updateUser() {
        userInit();
        userUpdate.setId(1);
        userStorage.updateUser(userUpdate);

        Optional<User> userUpdated = userStorage.getUser(1);
        assertThat(userUpdated).isPresent()
                .hasValueSatisfying(u -> assertThat(u)
                        .hasFieldOrPropertyWithValue("email", "user_Update@mail.ru"))
                .hasValueSatisfying(u -> assertThat(u)
                        .hasFieldOrPropertyWithValue("login", "user_Update_login"))
                .hasValueSatisfying(u -> assertThat(u)
                        .hasFieldOrPropertyWithValue("name", "user_Update_name"))
                .hasValueSatisfying(u -> assertThat(u)
                        .hasFieldOrPropertyWithValue("birthday",
                                LocalDate.of(1983, 4, 25)));

    }

    @Test
    public void deleteUser() {
        User user4 = new User("user_4_name", "user_4@mail.ru",
                "user_4_login", LocalDate.of(1981, 4, 25));
        userStorage.addUser(user4);
        List<User> userList = new ArrayList<>(userStorage.getAllUsers());

        userStorage.deleteUser(6);
        List<User> userListNew = new ArrayList<>(userStorage.getAllUsers());
        Assertions.assertEquals(userList.size() - 1, userListNew.size());
        Assertions.assertFalse(userList.contains(user1));

    }

    @Test
    public void testFriendship() {
        userInit();
        List<User>users = new ArrayList<>(userStorage.getAllUsers());

        userStorage.addFriend(1, 2);
        userStorage.addFriend(1,3);
        userStorage.addFriend(2,3);
        userStorage.addFriend(2,4);

        user1 = userStorage.getUser(1).get();
        user2 = userStorage.getUser(2).get();
        userService.getCommonFriends(1,2);

        Assertions.assertTrue(user1.getFriends().contains(user2));
        Assertions.assertFalse(user2.getFriends().contains(user1));

        userStorage.deleteFriend(1, 2);
        userStorage.deleteFriend(1, 3);
        Assertions.assertTrue(userStorage.getUser(1).get().getFriends().isEmpty());

    }

    @Test
    public void likeAndUnlikeFilm() {
        filmService.likeFilm(1, 1);
        Assertions.assertEquals(filmStorage.getFilm(1).get().getLikes().size(), 1);
        filmService.unlikeFilm(1, 1);
        Assertions.assertEquals(filmStorage.getFilm(1).get().getLikes().size(), 0);

        Assertions.assertThrows(UserNotFoundException.class, ()->filmService.likeFilm(1, 100));
        Assertions.assertThrows(FilmNotFoundException.class, ()->filmService.likeFilm(100, 1));
    }

    @Test
    public void getMostPopularFilm() {
        film1 = new Film("Начало", "Фильм с Ди Каприо",
                LocalDate.of(2000, 1, 1),
                120, new Mpa("G", 1));
        film4 = new Film("Терминатор", "Фильм про роботов",
                LocalDate.of(1995, 1, 1),
                130, new Mpa("R", 4));
        filmStorage.likeFilm(1, 1);
        filmStorage.likeFilm(1, 2);
        filmStorage.likeFilm(1, 4);
        filmStorage.likeFilm(2, 1);
        filmStorage.likeFilm(2, 2);
        filmStorage.likeFilm(3, 1);

        Optional<Film> mostPopularFilm = Optional.ofNullable(filmStorage.getMostPopularFilm(10).get(0));

        assertThat(mostPopularFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Начало"))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "Фильм с Ди Каприо"))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                                LocalDate.of(2000, 1, 1)))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("mpa", new Mpa("G", 1)))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 120)
                );
    }

}