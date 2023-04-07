package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Primary
@Component("mpaDbStorage")
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getMpaById(int id) {
        String sqlQueryMpaId = "select * from Mpa where id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQueryMpaId, this::mapRowToMpa, id);
        } catch (EmptyResultDataAccessException e) {
            throw new MpaNotFoundException("MPA  с id " + id + " не найден");
        }
    }

    public List<Mpa> getAllMpa() {
        String sqlQueryAllMpa = "select * from Mpa";
        return jdbcTemplate.query(sqlQueryAllMpa, this::mapRowToMpa);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getString("name"), resultSet.getInt("id"));
    }

}
