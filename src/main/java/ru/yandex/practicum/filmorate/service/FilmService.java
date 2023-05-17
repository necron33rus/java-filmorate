package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film == null) {
            throw new NotFoundException("FilmService: Фильм с идентификатором " + filmId + " не найден");
        } else if (user == null) {
            throw new NotFoundException("FilmService: Пользователь с идентификатором " + userId + " не найден");
        } else {
            film.getLikes().add(userId);
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
            film.getLikes().remove(userId);
            log.debug("FilmService: К объекту 'Фильм' с  идентификатором {} пользователем с " +
                    "идентификатором {} добавлен лайк", filmId, userId);
        } else {
            throw new NotFoundException("FilmService: Лайк от Пользователя с идентификатором " + userId +
                    " к Фильму с идентификатором " + filmId + " не найден");
        }
    }

    public List<Film> getPopularFilms(int count) {
        if (count == 0) {
            return filmStorage.getFilms().stream()
                    .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                    .limit(10)
                    .collect(Collectors.toList());
        } else {
            return filmStorage.getFilms().stream()
                    .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                    .limit(count)
                    .collect(Collectors.toList());
        }
    }
}
