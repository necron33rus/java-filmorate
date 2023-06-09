package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.RatingService;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@Component("filmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final RatingService ratingService;
    private final GenreService genreService;
    private final LikeStorage likeStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, RatingService ratingService, GenreService genreService,
                         LikeStorage likeStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.ratingService = ratingService;
        this.genreService = genreService;
        this.likeStorage = likeStorage;
    }

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
                ratingService.getRatingById(rs.getInt("rating_id")),
                genreService.getFilmGenres(rs.getLong("id"))
        ));
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("FilmDbStorage: Передан пустой аргумент");
        }
        String sqlString = "SELECT * FROM FILMS WHERE ID = ?";
        Film film;
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sqlString, filmId);
        if (rows.first()) {
            Rating rating = ratingService.getRatingById(rows.getInt("rating_id"));
            Set<Genre> genres = genreService.getFilmGenres(filmId);
            film = new Film(
                    rows.getLong("id"),
                    rows.getString("name"),
                    rows.getString("description"),
                    rows.getDate("release_date").toLocalDate(),
                    rows.getInt("duration"),
                    new HashSet<>(likeStorage.getLikes(rows.getLong("id"))),
                    rating,
                    genres
            );
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
        film.setMpa(ratingService.getRatingById(film.getMpa().getId()));
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genre.setName(genreService.getGenreById(genre.getId()).getName());
            }
            genreService.putGenres(film);
        }
        log.info("FilmDbStorage: Добавлен новый фильм с ID = {}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film == null) {
            throw new ValidationException("FilmDbStorage: Передан пустой аргумент");
        }
        String sqlString = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ? " +
                "WHERE ID = ?";
        if (jdbcTemplate.update(sqlString,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()) != 0) {
            film.setMpa(ratingService.getRatingById(film.getMpa().getId()));
            if (film.getGenres() != null) {
                Collection<Genre> sortGenres = film.getGenres().stream()
                        .sorted(Comparator.comparing(Genre::getId))
                        .collect(Collectors.toList());
                film.setGenres(new LinkedHashSet<>(sortGenres));
                for (Genre genre : film.getGenres()) {
                    genre.setName(genreService.getGenreById(genre.getId()).getName());
                }
            }
            genreService.putGenres(film);
            log.info("FilmDbStorage: Фильм с ID = {} успешно обновлен", film.getId());
            return film;
        } else {
            throw new NotFoundException("FilmDbStorage: Фильм с ID=" + film.getId() + " не найден!");
        }
    }

    @Override
    public void deleteFilm(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("FilmDbStorage: Передан пустой аргумент");
        }
        String sqlString = "DELETE FROM FILMS WHERE ID = ?";
        if (jdbcTemplate.update(sqlString, filmId) == 0) {
            throw new NotFoundException("FilmDbStorage: Фильм с ID=" + filmId + " не найден!");
        }
        log.info("FilmDbStorage: Фильм с ID = {} успешно удален", filmId);
    }
}
