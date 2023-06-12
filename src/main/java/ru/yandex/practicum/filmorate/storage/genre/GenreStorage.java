package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import javax.validation.ValidationException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public void addGenre(String name) {
        if (StringUtils.isBlank(name)) {
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
