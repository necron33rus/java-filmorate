package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import javax.validation.ValidationException;
import java.util.List;

@Component
public class GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addGenre(String name) {
        if (name == null) {
            throw new ValidationException("GenreStorage: Передан пустой аргумент");
        }
        String sqlString = "INSERT INTO GENRES (NAME) VALUES (?)";
        jdbcTemplate.update(sqlString, name);
    }

    public void deleteGenre(Integer genreId) {
        if (genreId == null) {
            throw new ValidationException("GenreStorage: Передан пустой аргумент");
        }
        String sqlString = "DELETE FROM GENRES WHERE ID = ?";
        jdbcTemplate.update(sqlString, genreId);
    }

    public List<Genre> getGenres() {
        String sqlString = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sqlString, (rs, rowNum) -> new Genre(
                rs.getInt("id"),
                rs.getString("name"))
        );
    }

    public Genre getGenreById(Integer genreId) {
        if (genreId == null) {
            throw new ValidationException("GenreStorage: Передан пустой аргумент");
        }

        Genre outputGenre;
        String sqlString = "SELECT * FROM GENRES WHERE ID = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sqlString, genreId);
        if (rows.first()) {
            outputGenre = new Genre(
                    rows.getInt("id"),
                    rows.getString("name")
            );
        } else {
            throw new NotFoundException("GenreStorage: Жанр с id = " + genreId + " не найден");
        }
        return outputGenre;
    }

    public void addFilmGenreReference(Film film) {
        if (film == null) {
            throw new ValidationException("GenreStorage: Передан пустой аргумент");
        }
        String sqlString = "INSERT INTO REF_FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlString, film.getId(), genre.getId());
            }
        }
    }

    public void deleteFilmGenreReference(Film film) {
        if (film == null) {
            throw new ValidationException("GenreStorage: Передан пустой аргумент");
        }
        String sqlString = "DELETE FROM REF_FILMS_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlString, film.getId());
    }

    public List<Genre> getFilmGenres(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("GenreStorage: Передан пустой аргумент");
        }
        String sqlString = "SELECT ID, NAME FROM GENRES JOIN REF_FILMS_GENRES ON ID = GENRE_ID WHERE FILM_ID = ?";
        return jdbcTemplate.query(sqlString, (rs, rowNum) -> new Genre(
                rs.getInt("id"),
                rs.getString("name")), filmId
        );
    }
}
