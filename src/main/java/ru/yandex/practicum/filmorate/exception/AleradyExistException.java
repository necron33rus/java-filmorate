package ru.yandex.practicum.filmorate.exception;

import java.rmi.AlreadyBoundException;

public class AleradyExistException extends AlreadyBoundException {
    public AleradyExistException(String s) {
        super(s);
    }
}
