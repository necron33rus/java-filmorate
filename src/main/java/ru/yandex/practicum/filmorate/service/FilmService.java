package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("likeDbStorage") LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    public void addLike(Long filmId, Long userId) {
        if (isFilmAndUserExist(filmId, userId)) {
            likeStorage.addLike(filmId, userId);
            log.debug("FilmService: К Фильму с  идентификатором {} Пользователем с идентификатором {} добавлен лайк", filmId, userId);
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        if (isFilmAndUserExist(filmId, userId)) {
            if (filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
                likeStorage.deleteLike(filmId, userId);
                log.debug("FilmService: К объекту 'Фильм' с  идентификатором {} пользователем с " +
                        "идентификатором {} добавлен лайк", filmId, userId);
            } else {
                throw new NotFoundException("FilmService: Лайк от Пользователя с идентификатором " + userId +
                        " к Фильму с идентификатором " + filmId + " не найден");
            }
        }
    }

    public List<Film> getPopularFilms(int count) {
        if (count < 1) {
            throw new ValidationException("Количество фильмов для вывода должно быть не меньше 1");
        }
        return filmStorage.getPopular(count);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        if (isFilmExist(film.getId())) {
            return filmStorage.updateFilm(film);
        } else {
            throw new NotFoundException("FilmService: Фильм с идентификатором " + film.getId() + " не найден");
        }
    }

    public Film getFilmById(Long filmId) {
        if (isFilmExist(filmId)) {
            return filmStorage.getFilmById(filmId);
        } else {
            throw new NotFoundException("FilmService: Фильм с идентификатором " + filmId + " не найден");
        }
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void deleteFilm(Long filmId) {
        if (isFilmExist(filmId)) {
            filmStorage.deleteFilm(filmId);
        } else {
            throw new NotFoundException("FilmService: Фильм с идентификатором " + filmId + " не найден");
        }
    }

    public void deleteAllFilms() {
        filmStorage.deleteAllFilms();
    }

    private boolean isFilmExist(Long filmId) {
        if (filmStorage.getFilms().contains(filmStorage.getFilmById(filmId))) {
            return true;
        } else {
            throw new ValidationException("Валидация: Фильм с идентификатором " + filmId + " не найден");
        }
    }

    private boolean isFilmAndUserExist(Long filmId, Long userId) {
        isFilmExist(filmId);
        if (userStorage.getAllUsers().contains(userStorage.getUserById(userId))) {
            return true;
        } else {
            throw new ValidationException("Валидация: Пользователь с идентификатором " + userId + " не найден");
        }
    }
}
