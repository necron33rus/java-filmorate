package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("FilmController: Получен GET запрос к эндпоинту /films");
        return filmStorage.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("FilmController: Получен GET запрос к эндпоинту /films/{}", id);
        return filmStorage.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(name = "count", defaultValue = "10") int count) {
        log.info("FilmController: Получен GET запрос к эндпоинту /films/popular");
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("FilmController: Получен POST запрос к эндпоинту /films. Тело запроса: {}", film);
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateOrCreateFilm(@Valid @RequestBody Film film) {
        log.info("FilmController: Получен PUT запрос к эндпоинту /films. Тело запроса: {}", film);
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("FilmController: Получен PUT запрос к эндпоинту /films/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("FilmController: Получен DELETE запрос к эндпоинту /films/{}/like/{}", id, userId);
        filmService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable Long id) {
        log.info("FilmController: Получен DELETE запрос к эндпоинту /films/{}", id);
        filmStorage.deleteFilm(id);
    }
}
