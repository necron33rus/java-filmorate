package ru.yandex.practicum.filmorate.exception;

public class FilmValidationException extends IllegalArgumentException {

    public FilmValidationException(String message) {
        super(message);
    }
}
