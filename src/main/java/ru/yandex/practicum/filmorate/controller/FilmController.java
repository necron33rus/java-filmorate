package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AleradyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.Collection;

@Validated
@RestController
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    FilmService filmService = new FilmService();

    @GetMapping("/films")
    public ResponseEntity<Collection<Film>> getAllFilms() {
        log.info("UserController: Получен GET запрос к эндпоинту /film ");
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @PostMapping("/films")
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) throws AleradyExistException {
        log.info("UserController: Получен POST запрос к эндпоинту /film. Тело запроса: {}", film);
        filmService.createFilm(film);
        return ResponseEntity.ok(film);
    }

    @PutMapping("/films")
    public ResponseEntity<Film> updateOrCreateUser(@Valid @RequestBody Film film) {
        log.info("UserController: Получен PUT запрос к эндпоинту /film. Тело запроса: {}", film);
        filmService.updateFilm(film);
        return ResponseEntity.ok(film);
    }

    @DeleteMapping("/films")
    public ResponseEntity<Collection<Film>> deleteAllFilms() {
        log.warn("UserController: Получен DELETE запрос к эндпоинту /films");
        return ResponseEntity.ok(filmService.deleteAllFilms());
    }
}
