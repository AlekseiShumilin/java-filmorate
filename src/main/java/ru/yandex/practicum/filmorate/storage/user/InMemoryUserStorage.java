package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    Integer userId = 0;

    public Integer generateId() {
        return ++userId;
    }

    @Override
    public void deleteFriend(int userId, int friendId) {

    }

    @Override
    public void deleteUser(int id) {
        users.remove(id);
    }

    @Override
    public List<User> getFriends(int userId) {
        return null;
    }

    @Override
    public void addFriend(int user, int friend) {

    }

    @Override
    public void addUser(User user) {
        if (users.containsValue(user)) {
            throw new UserAlreadyExistException("User with name " + user.getName() + " already exist.");
        } else {
            user.setId(generateId());
            users.put(user.getId(), user);
        }
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            log.info("userUpdate {}. User with id " + user.getId() + " is not exist.", user);
            throw new UserNotFoundException("User with id " + user.getId() + " is not exist.");
        }
        return getUser(user.getId());
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUser(Integer id) {
        if (!users.containsKey(id)) {
            log.info("getUser {}. User with id " + id + " is not exist.", id);
            throw new UserNotFoundException("User with id " + id + " is not exist.");
        } else {
            return Optional.of(users.get(id));
        }

    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        return null;
    }
}
