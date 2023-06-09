package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films;
    private Long currentId;

    public InMemoryFilmStorage() {
        films = new HashMap<>();
        currentId = 0L;
    }

    @Override
    public List<Film> getFilms() {
        log.debug("InMemoryFilmStorage: Массив объектов передан в ответ на запрос");
        return List.copyOf(films.values());
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("InMemoryFilmStorage: Объект с идентификатором " + filmId + " не найден");
        } else {
            log.debug("InMemoryFilmStorage: Объект с идентификатором {} передан в ответ на запрос", filmId);
            return films.get(filmId);
        }
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(++currentId);
        films.put(film.getId(), film);
        log.debug("InMemoryFilmStorage: Создан объект {} с идентификатором {}", film, film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getId() == 0) {
            throw new FilmValidationException("InMemoryFilmStorage: Передан пустой аргумент");
        } else if (!films.containsKey(film.getId())) {
            throw new NotFoundException("InMemoryFilmStorage: Объект с идентификатором " + film.getId() + " не найден");
        } else {
            films.put(film.getId(), film);
            log.debug("InMemoryFilmStorage: Объект с идентификатором {} обновлен", film.getId());
        }
        return film;
    }

    @Override
    public void deleteFilm(Long filmId) {
    }
}
