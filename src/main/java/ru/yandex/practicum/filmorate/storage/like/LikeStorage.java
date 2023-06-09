package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.RatingService;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.List;

@Component
public class LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RatingService ratingService;
    private final GenreService genreService;

    @Autowired
    public LikeStorage(JdbcTemplate jdbcTemplate, RatingService ratingService, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.ratingService = ratingService;
        this.genreService = genreService;
    }

    public void addLike(Long filmId, Long userId) {
        if (filmId == null || userId == null) {
            throw new ValidationException("LikeStorage: Передан пустой аргумент");
        }
        String sqlString = "INSERT INTO REF_FILMS_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlString, filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        if (filmId == null || userId == null) {
            throw new ValidationException("LikeStorage: Передан пустой аргумент");
        }
        String sqlString = "DELETE FROM REF_FILMS_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlString, filmId, userId);
    }

    public List<Long> getLikes(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("LikeStorage: Передан пустой аргумент");
        }
        String sqlString = "SELECT USER_ID FROM REF_FILMS_LIKES WHERE FILM_ID = ?";
        return jdbcTemplate.query(sqlString,
                (rs, rowNum) -> rs.getLong("user_id"),filmId);
    }

    public List<Film> getPopular(Integer count) {
        String sqlString = "SELECT ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID FROM FILMS " +
                "LEFT JOIN REF_FILMS_LIKES ON FILMS.ID = REF_FILMS_LIKES.FILM_ID " +
                "GROUP BY FILMS.ID ORDER BY COUNT(REF_FILMS_LIKES.USER_ID) DESC LIMIT ?";
        return jdbcTemplate.query(sqlString, (rs, rowNum) -> new Film(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getInt("duration"),
                        new HashSet<>(getLikes(rs.getLong("id"))),
                        ratingService.getRatingById(rs.getInt("rating_id")),
                        genreService.getFilmGenres(rs.getLong("id"))),
                count);
    }
}
