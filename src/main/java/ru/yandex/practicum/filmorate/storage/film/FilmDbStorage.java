package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private static int filmId = 0;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFilm(Film film) {
        film.setId(++filmId);

        String sqlQueryFilm = "insert into films (id, name, description, release_date, duration, id_mpa) " +
                "Values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQueryFilm,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String sqlQueryGenres = "insert into genres_films (genre_id, film_id) values (?, ?)";
                jdbcTemplate.update(sqlQueryGenres,
                        genre.getId(), film.getId());
            }
        }
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        String sqlQueryFilm = "update films set name = ?, description = ?," +
                " release_date = ?, duration = ?, id_mpa = ? where id = ?";
        int rowsNum = jdbcTemplate.update(sqlQueryFilm,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (rowsNum == 0) {
            log.info("Фильм с идентификатором {} не найден.", film.getId());
            return Optional.empty();
        } else {

            if (film.getGenres() != null) {
                if (film.getGenres().isEmpty()) {
                    String sqlQueryGenres = "DELETE FROM GENRES_FILMS WHERE film_id = ?";
                    jdbcTemplate.update(sqlQueryGenres,
                            film.getId());
                }
                String sqlQueryGenresId = "SELECT genre_id FROM genres_films WHERE FILM_ID = ?";
                List<Integer> genresId = jdbcTemplate.query(sqlQueryGenresId,
                        this::mapRowToGenreFilmsGenreId, film.getId());
                for (Genre genre : film.getGenres()) {
                    if (!genresId.contains(genre.getId())) {
                        String sqlQueryGenres = "insert into genres_films (genre_id, film_id) values (?, ?)";
                        jdbcTemplate.update(sqlQueryGenres,
                                genre.getId(), film.getId());
                    }
                }
                List<Integer> updatedGenresId = film.getGenres().stream()
                        .map(Genre::getId)
                        .collect(Collectors.toList());
                for (Integer id : genresId) {
                    if (!updatedGenresId.contains(id)) {
                        String sqlQueryGenres = "DELETE FROM GENRES_FILMS WHERE genre_id = ? and film_id = ?;";
                        jdbcTemplate.update(sqlQueryGenres,
                                id, film.getId());
                    }
                }
            }
        }
        return getFilm(film.getId());
    }

    @Override

    public Collection<Film> getAllFilms() {
        String sqlQueryfilmIdList = "select id from films order by id";

        return jdbcTemplate.queryForStream(sqlQueryfilmIdList, this::mapRowToFilmId)
                .map(this::getFilm)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Film> getFilm(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);
        String sqlQueryGenres = "SELECT *  FROM GENRES g" +
                " JOIN GENRES_FILMS gf ON g.ID = gf.GENRE_ID WHERE gf.FILM_ID = ?;";
        String sqlQueryLikes = "SELECT ID_user FROM FILMS_USERS fu WHERE ID_FILM  = ?";
        String sqlQueryMpa = "SELECT m.ID, m.NAME  FROM mpa AS m JOIN films AS f ON m.ID = f.ID_MPA WHERE f.ID = ?";
        if (filmRows.next()) {
            Film film = new Film(filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    new Mpa(null, 0));

            film.setId(filmRows.getInt("id"));
            film.setGenres(jdbcTemplate.query(sqlQueryGenres, this::mapRowToGenre, id));
            film.setLikes(jdbcTemplate.query(sqlQueryLikes, this::mapRowToFriends, id));
            film.setMpa(jdbcTemplate.queryForObject(sqlQueryMpa, this::mapRowToMpa, id));


            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else log.info("Фильм с идентификатором {} не найден.", id);
        return Optional.empty();
    }

    @Override

    public List<Film> getMostPopularFilm(int count) {
        String sqlQuery = "SELECT f.id, count(fu.ID_USER) FROM FILMS f JOIN FILMS_USERS fu oN f.id = fu.ID_FILM" +
                " GROUP BY f.NAME" +
                " ORDER BY COUNT(fu.ID_USER) DESC LIMIT ?";
        List<Integer> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilmId, count);
        if (films.isEmpty()) {
            log.info("Список фильмов пуст");
            return new ArrayList<>(getAllFilms());
        } else {
            return films.stream()
                    .map(this::getFilm)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void deleteFilm(int id) {
        String sqlQuery = "delete from films where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        String sqlQuery = "insert into films_users  (id_film, id_user) values(?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void unlikeFilm(int filmId, int userId) {
        String sqlQuery = "delete from films_users where id_film = ? and id_user = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    private int mapRowToFilmId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("id");
    }

    private long mapRowToFriends(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("id_user");
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getString("name"), resultSet.getInt("id"));
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getString("name"), resultSet.getInt("id"));
    }

    private Integer mapRowToGenreFilmsGenreId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("genre_id");
    }


}
