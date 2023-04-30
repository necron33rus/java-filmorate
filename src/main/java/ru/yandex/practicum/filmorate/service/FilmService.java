package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AleradyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId;

    public Collection<Film> getAllFilms() {
        log.debug("UserService: Массив объектов передан в ответ на запрос");
        return List.copyOf(films.values());
    }

    public void createFilm(Film film) {
        if (films.containsKey(film.getId())) {
            log.warn("UserService: Объект с идентификатором {} был уже создан", film.getId());
            throw new AleradyExistException("Объект с идентификатором " + film.getId() + " был уже создан");
        } else {
            film.setId(++currentId);
            films.put(film.getId(), film);
            log.debug("UserService: Создан объект {} с идентификатором {}", film, film.getId());
        }
    }

    public void updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("UserService: Объект с идентификатором {} обновлен", film.getId());
        } else {
            log.debug("UserService: Объект с идентификатором {} не найден и будет создан", film.getId());
            throw new NotFoundException("Объект с идентификатором " + film.getId() + " не найден");
        }
    }
}
