package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private LikeStorage likeStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film == null) {
            throw new NotFoundException("FilmService: Фильм с идентификатором " + filmId + " не найден");
        } else if (user == null) {
            throw new NotFoundException("FilmService: Пользователь с идентификатором " + userId + " не найден");
        } else {
            likeStorage.addLike(filmId, userId);
            log.debug("FilmService: К Фильму с  идентификатором {} Пользователем с идентификатором {} добавлен лайк", filmId, userId);
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film == null) {
            throw new NotFoundException("FilmService: Фильм с идентификатором " + filmId + " не найден");
        } else if (user == null) {
            throw new NotFoundException("FilmService: Пользователь с идентификатором " + userId + " не найден");
        } else if (film.getLikes().contains(userId)) {
            likeStorage.deleteLike(filmId, userId);
            log.debug("FilmService: К объекту 'Фильм' с  идентификатором {} пользователем с " +
                    "идентификатором {} добавлен лайк", filmId, userId);
        } else {
            throw new NotFoundException("FilmService: Лайк от Пользователя с идентификатором " + userId +
                    " к Фильму с идентификатором " + filmId + " не найден");
        }
    }

    public List<Film> getPopularFilms(int count) {
        if (count < 1) {
            new ValidationException("Количество фильмов для вывода должно быть не меньше 1");
        }
        return likeStorage.getPopular(count);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }
}
