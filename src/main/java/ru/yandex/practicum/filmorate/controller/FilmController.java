package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    @Autowired
    private FilmService filmService;

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("FilmController: Получен GET запрос к эндпоинту /film ");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("FilmController: Получен GET запрос к эндпоинту /film/{}", id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(name = "count", defaultValue = "10") int count) {
        log.info("FilmController: Получен GET запрос к эндпоинту /film/popular");
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("FilmController: Получен POST запрос к эндпоинту /film. Тело запроса: {}", film);
        filmService.createFilm(film);
        return film;
    }

    @PutMapping
    public Film updateOrCreateFilm(@Valid @RequestBody Film film) {
        log.info("FilmController: Получен PUT запрос к эндпоинту /film. Тело запроса: {}", film);
        filmService.updateFilm(film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("FilmController: Получен PUT запрос к эндпоинту /film/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("FilmController: Получен DELETE запрос к эндпоинту /film/{}/like/{}", id, userId);
        filmService.deleteLike(id, userId);
    }
}
