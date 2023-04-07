package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component("userDbStorage")
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static int id = 0;


    @Override
    public void addUser(User user) {
        user.setId(++id);
        String sqlQueryGetUser = "insert into users (id, email, login, name, birthday) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQueryGetUser, user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setFriends(new ArrayList<>());
    }

    @Override
    public Optional<User> updateUser(User user) {
        String sqlQueryGetUser = "update users set email = ?, login = ?, name = ?, birthday = ? where id = ?";
        int rowsNum = jdbcTemplate.update(sqlQueryGetUser,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        if (rowsNum == 0) {
            return Optional.empty();
        } else {
            return getUser(user.getId());
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlQueryAllUsers = "select * from users order by id";
        return jdbcTemplate.queryForStream(sqlQueryAllUsers, this::mapRawToUserId)
                .map(this::getUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> getUser(Integer id) {
        User user;
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        String sqlFriends = "select friend_id from friendship where user_id = ?";
        if (userRows.next()) {
            user = new User(userRows.getString("name"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getDate("birthday").toLocalDate());

            user.setId(userRows.getInt("id"));
            user.setFriends(new ArrayList<>(jdbcTemplate.queryForStream(sqlFriends, this::mapRawToFriendId, id)
                    .map(this::getUser)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList())));
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    public void addFriend(int user, int friend) {
        if (getUser(user).isEmpty() || getUser(friend).isEmpty()) {
            throw new UserNotFoundException("Пользователи не найдены");
        } else {
            String sqlQueryAddFriend = "insert into friendship (user_id, friend_id) values (?, ?)";
            jdbcTemplate.update(sqlQueryAddFriend, user, friend);
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "Delete from friendship where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getFriends(int userId) {
        String findFriends = "select friend_id from friendship where user_id = ?";
        return jdbcTemplate.queryForStream(findFriends, this::mapRawToFriendId, userId)
                .map(this::getUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(int id) {
        String sqlQuery = "delete from users where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    private int mapRawToUserId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("id");
    }

    private int mapRawToFriendId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("friend_id");
    }
}
