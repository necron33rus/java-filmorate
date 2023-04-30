package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionApiHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundException(NotFoundException exception) {
        List<String> errors = Collections.singletonList(exception.getMessage());
        log.warn("Ошибка выполнения запроса: {}", errors);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(getErrorsMap(errors)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> mismatchException(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        log.warn("Ошибка выполнения запроса: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(getErrorsMap(errors)));
    }

    @ExceptionHandler(FilmValidationException.class)
    public ResponseEntity<ErrorMessage> badAttributeValueException(FilmValidationException exception) {
        List<String> errors = Collections.singletonList(exception.getMessage());
        log.warn("Ошибка выполнения запроса: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(getErrorsMap(errors)));
    }

    @ExceptionHandler(AleradyExistException.class)
    public ResponseEntity<ErrorMessage> alreadyExistException(AleradyExistException exception) {
        List<String> errors = Collections.singletonList(exception.getMessage());
        log.warn("Ошибка выполнения запроса: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(getErrorsMap(errors)));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorMessage> unhandleException(Throwable exception) {
        List<String> errors = Collections.singletonList(exception.getMessage());
        log.warn("Ошибка выполнения запроса: {}", errors);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(getErrorsMap(errors)));
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorsMap = new HashMap<>();
        errorsMap.put("errors", errors);
        return errorsMap;
    }
}
