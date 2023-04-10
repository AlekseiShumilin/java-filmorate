package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Primary
@Component("genreDbStorage")
@RequiredArgsConstructor
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public Genre getGenreById(int id) {
        String sqlQueryGenre = "select * from genres where id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQueryGenre, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNotFoundException("Жанр с id " + id + " не найден.");
        }
    }

    public List<Genre> getAllGenres() {
        String sqlQueryAllGenres = "select * from genres";
        return jdbcTemplate.query(sqlQueryAllGenres, this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getString("name"), resultSet.getInt("id"));
    }
}
