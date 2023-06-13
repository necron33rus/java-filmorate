package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component("filmDbStorage")
@Slf4j
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final RatingStorage ratingStorage;
    private final LikeStorage likeStorage;

    @Override
    public List<Film> getFilms() {
        String sqlString = "SELECT * FROM FILMS";
        return jdbcTemplate.query(sqlString, (rs, rowNum) -> new Film(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new HashSet<>(likeStorage.getLikes(rs.getLong("id"))),
                ratingStorage.getRatingById(rs.getInt("rating_id")),
                getFilmGenres(rs.getLong("id"))
        ));
    }

    @Override
    public Film getFilmById(Long filmId) {
        String sqlString = "SELECT * FROM FILMS WHERE ID = ?";
        Film film;
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sqlString, filmId);
        if (rows.first()) {
            Set<Genre> genres = getFilmGenres(filmId);
            film = new Film(
                    rows.getLong("id"),
                    rows.getString("name"),
                    rows.getString("description"),
                    Objects.requireNonNull(rows.getDate("release_date")).toLocalDate(),
                    rows.getInt("duration"),
                    new HashSet<>(likeStorage.getLikes(rows.getLong("id"))),
                    null,
                    genres
            );
            film.setMpa(ratingStorage.getRatingById(rows.getInt("rating_id")));
        } else {
            throw new NotFoundException("FilmDbStorage: Фильм с ID=" + filmId + " не найден!");
        }
        return film;
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("ID");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
        film.setMpa(ratingStorage.getRatingById(film.getMpa().getId()));
        if (film.getGenres() != null) {
            addFilmGenreReference(film.getId(), film.getGenres());
        }
        log.info("FilmDbStorage: Добавлен новый фильм с ID = {}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlString = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ? " +
                "WHERE ID = ?";
        jdbcTemplate.update(sqlString,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        putGenres(film.getId(), film.getGenres());
        log.info("FilmDbStorage: Фильм с ID = {} успешно обновлен", film.getId());
        return getFilmById(film.getId());
    }

    @Override
    public void deleteFilm(Long filmId) {
        String sqlString = "DELETE FROM FILMS WHERE ID = ?";
        jdbcTemplate.update(sqlString, filmId);
        log.info("FilmDbStorage: Фильм с ID = {} успешно удален", filmId);
    }

    public void deleteAllFilms() {
        String sqlString = "DELETE FROM FILMS";
        jdbcTemplate.update(sqlString);
        log.info("FilmDbStorage: все фильмы успешно удалены");
    }

    @Override
    public Set<Genre> getFilmGenres(Long filmId) {
        String sqlString = "SELECT ID, NAME FROM GENRES JOIN REF_FILMS_GENRES ON ID = GENRE_ID WHERE FILM_ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlString, filmId);
        Set<Genre> genres = new HashSet<>();
        while (sqlRowSet.next()) {
            genres.add(new Genre(sqlRowSet.getInt("id"),
                    sqlRowSet.getString("name")));
        }
        return genres;
    }

    public void addFilmGenreReference(Long filmId, Set<Genre> genres) {
        String sqlString = "INSERT INTO REF_FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
        for (Genre genre : genres) {
            jdbcTemplate.update(sqlString, filmId, genre.getId());
        }
    }

    public void deleteFilmGenreReference(Long filmId) {
        String sqlString = "DELETE FROM REF_FILMS_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlString, filmId);
    }

    private void putGenres(Long filmId, Set<Genre> genres) {
        deleteFilmGenreReference(filmId);
        addFilmGenreReference(filmId, genres);
    }

    @Override
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
                        new HashSet<>(likeStorage.getLikes(rs.getLong("id"))),
                        ratingStorage.getRatingById(rs.getInt("rating_id")),
                        getFilmGenres(rs.getLong("id"))),
                count);
    }
}
