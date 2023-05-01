package ru.yandex.practicum.filmorate.exception;

public class AleradyExistException extends RuntimeException {
    public AleradyExistException(String message) {
        super(message);
    }
}
