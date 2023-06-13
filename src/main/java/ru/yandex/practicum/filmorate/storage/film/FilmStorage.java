package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    List<Film> getFilms();

    Film getFilmById(Long filmId);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Long filmId);

    Set<Genre> getFilmGenres(Long filmId);

    List<Film> getPopular(Integer count);

    void deleteAllFilms();
}
