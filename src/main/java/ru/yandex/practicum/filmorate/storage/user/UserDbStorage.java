package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.beans.Transient;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("userDbStorage")
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

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
        Stream<User> users = jdbcTemplate.queryForStream(sqlQueryAllUsers, this::mapRawToUserId)
                .map(this::getUser)
                .filter(Optional::isPresent)
                .map(Optional::get);
        List<User> userList = users.collect(Collectors.toList());
        users.close();
        return userList;
    }

    @Transient

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
            Stream<User> friendsList = jdbcTemplate.queryForStream(sqlFriends, this::mapRawToFriendId, id)
                    .map(this::getUser)
                    .filter(Optional::isPresent)
                    .map(Optional::get);
            List<User> friends = friendsList.collect(Collectors.toList());
            friendsList.close();
            user.setFriends(friends);

            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Transient

    public void addFriend(int user, int friend) {
        String sqlQueryAddFriend = "insert into friendship (user_id, friend_id) values (?, ?)";
        jdbcTemplate.update(sqlQueryAddFriend, user, friend);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "Delete from friendship where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Transient

    @Override
    public List<User> getFriends(int userId) {
        String findFriends = "select friend_id from friendship where user_id = ?";
        Stream<User> userStream = jdbcTemplate.queryForStream(findFriends, this::mapRawToFriendId, userId)
                .map(this::getUser)
                .filter(Optional::isPresent)
                .map(Optional::get);
        List<User> userList = userStream.collect(Collectors.toList());
        userStream.close();
        return userList;
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        String sql = "SELECT friend_id FROM friendship" +
                " WHERE user_id = ? AND FRIEND_ID IN (SELECT friend_id FROM friendship WHERE user_id = ?)";

        Stream<User> userStream = jdbcTemplate.queryForStream(sql, this::mapRawToFriendId, userId, friendId)
                .map(this::getUser)
                .filter(Optional::isPresent)
                .map(Optional::get);
        List<User> userList = userStream.collect(Collectors.toList());
        userStream.close();
        return userList;
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
