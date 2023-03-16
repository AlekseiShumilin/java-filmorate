package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class User {
    private Integer id;
    private final String email;
    private final String login;
    private String name;
    private Set<Integer> friends = new HashSet<>();

    public User(String name, String email, String login, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    private final LocalDate birthday;

    public void addFriend(Integer friendId) {
        this.friends.add(friendId);
    }

    public void removeFriend(Integer friendId) {
        this.friends.remove(friendId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getLogin(),
                user.getLogin()) && Objects.equals(getName(), user.getName()) && Objects.equals(getFriends(),
                user.getFriends()) && Objects.equals(getBirthday(), user.getBirthday());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getLogin(), getName(), getFriends(), getBirthday());
    }
}
