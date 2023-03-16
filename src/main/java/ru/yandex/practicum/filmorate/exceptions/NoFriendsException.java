package ru.yandex.practicum.filmorate.exceptions;

public class NoFriendsException extends RuntimeException {
    public NoFriendsException(final String message) {
        super(message);
    }
}
