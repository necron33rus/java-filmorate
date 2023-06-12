package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {
    @Autowired
    private GenreStorage genreStorage;

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

}
