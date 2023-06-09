package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {
    private GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> getGenres() {
        return genreStorage.getGenres().stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toList());
    }

    public Genre getGenreById(Integer genreId) {
        return genreStorage.getGenreById(genreId);
    }

    public void addGenre(String genreName) {
        genreStorage.addGenre(genreName);
    }

    public void deleteGenre(Integer genreId) {
        genreStorage.deleteGenre(genreId);
    }

    public Set<Genre> getFilmGenres(Long filmId) {
        return new HashSet<>(genreStorage.getFilmGenres(filmId));
    }

    public void putGenres(Film film) {
        genreStorage.deleteFilmGenreReference(film);
        genreStorage.addFilmGenreReference(film);
    }
}
