package ru.yandex.practicum.filmorate.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
@Component
@Slf4j
public class UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    int userId = 0;
    public Integer generateId() {
        return ++userId;
    }
    public void addUser(User user){
        user.setId(generateId());
        users.put(user.getId(), user);
    }
    public void updateUser(User user){
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            log.info("userUpdate {}. User with id " + user.getId() + "is not exist.", user);
            throw new ValidationException("User with id " + user.getId() + "is not exist.");
        }
    }
    public Collection<User> getAllUsers(){
        return users.values();
    }
    public User getUser(Integer id) {
        return users.get(id);
    }

}
