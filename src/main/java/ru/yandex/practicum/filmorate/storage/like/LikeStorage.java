package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("likeDbStorage")
public class LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLike(Long filmId, Long userId) {
        String sqlString = "INSERT INTO REF_FILMS_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlString, filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        String sqlString = "DELETE FROM REF_FILMS_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlString, filmId, userId);
    }

    public List<Long> getLikes(Long filmId) {
        String sqlString = "SELECT USER_ID FROM REF_FILMS_LIKES WHERE FILM_ID = ?";
        return jdbcTemplate.query(sqlString,
                (rs, rowNum) -> rs.getLong("user_id"), filmId);
    }
}
