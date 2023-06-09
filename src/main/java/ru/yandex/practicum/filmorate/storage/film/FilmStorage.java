package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();

    Film getFilmById(Long filmId);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Long filmId);
}
