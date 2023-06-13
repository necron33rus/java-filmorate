package ru.yandex.practicum.filmorate.storage.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import javax.validation.ValidationException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RatingStorage {
    private final JdbcTemplate jdbcTemplate;

    public List<Rating> getAllRatings() {
        String sqlString = "SELECT * FROM RATING";
        return jdbcTemplate.query(sqlString, (rs, rowNum) -> new Rating(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"))
        );
    }

    public Rating getRatingById(Integer ratingId) {
        if (ratingId == null) {
            throw new ValidationException("RatingStorage: Передан пустой аргумент");
        }
        Rating rating;
        String sqlString = "SELECT * FROM RATING WHERE ID = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sqlString, ratingId);
        if (rows.first()) {
            rating = new Rating(
                    rows.getInt("id"),
                    rows.getString("name"),
                    rows.getString("description")
            );
        } else {
            throw new NotFoundException("RatingStorage: Рейтинг с id = " + ratingId + " не найден");
        }
        return rating;
    }
}
