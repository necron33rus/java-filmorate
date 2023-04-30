package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AleradyExistException extends RuntimeException {
    public AleradyExistException(String message) {
        super(message);
        log.warn(message);
    }
}
