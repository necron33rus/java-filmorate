package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilmValidationException extends IllegalArgumentException {

    public FilmValidationException(String message) {
        super(message);
        log.warn(message);
    }
}
