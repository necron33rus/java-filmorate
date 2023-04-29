package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AleradyExistException;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();
    private int increment = 0;

    public Collection<Film> getAllFilms() {
        log.debug("UserService: Массив объектов передан в ответ на запрос");
        return films.values();
    }

    public void createFilm(Film film) throws AleradyExistException {
        validate(film);
        if(films.containsKey(film.getId())) {
            log.warn("UserService: Объект с идентификатором {} был уже создан", film.getId());
            throw new AleradyExistException("Объект с идентификатором " + film.getId() + "был уже создан");
        } else {
            films.put(film.getId(), film);
            log.debug("UserService: Создан объект {} с идентификатором {}", film, film.getId());
        }
    }

    public void updateFilm(Film film) {
        validate(film);
        if(films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("UserService: Объект с идентификатором {} обновлен", film.getId());
        } else {
            log.debug("UserService: Объект с идентификатором {} не найден и будет создан", film.getId());
            throw new NotFoundException("Объект с идентификатором " + film.getId() + " не найден");
        }
    }

    public Collection<Film> deleteAllFilms() {
        films.clear();
        return films.values();
    }

    private void validate(Film film){
        if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("UserService: Дата релиза {} раньше 28.12.1895г", film.getReleaseDate());
            throw new FilmValidationException("Дата релиза " + film.getReleaseDate() + " раньше 28.12.1895г");
        }
        if(film.getId() == 0) {
            film.setId(++increment);
        }
        if(film.getDescription().length() > 200) {
            log.warn("UserService: Длина описания превышает 200 символов");
            throw new FilmValidationException("Длина описания превышает 200 символов");
        }
    }
}
