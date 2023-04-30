package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AleradyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    @Autowired
    private FilmService filmService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("UserController: Получен GET запрос к эндпоинту /film ");
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) throws AleradyExistException {
        log.info("UserController: Получен POST запрос к эндпоинту /film. Тело запроса: {}", film);
        filmService.createFilm(film);
        return film;
    }

    @PutMapping
    public Film updateOrCreateUser(@Valid @RequestBody Film film) {
        log.info("UserController: Получен PUT запрос к эндпоинту /film. Тело запроса: {}", film);
        filmService.updateFilm(film);
        return film;
    }
}
