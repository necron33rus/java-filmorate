package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmServiceTest {
    private static FilmService filmService;
    private static Film film;

    @BeforeAll
    public static void beforeAll() {
        film = new Film();
        filmService = new FilmService();
    }

    @Test
    void shouldAddWhenAddValidFilmData() {
        film.setId(1);
        film.setName("Correct Name");
        film.setDescription("Correct description.");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100);
        filmService.createFilm(film);
        assertEquals(1, film.getId());
    }

    @Test
    void shouldThrowExceptionWhenAddFailedFilmReleaseDate() {
        film.setId(new Random().nextInt(50));
        film.setName("Correct Name");
        film.setDescription("Correct description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(100);
        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> filmService.createFilm(film));
        assertEquals("Дата релиза 1895-12-27 раньше 28.12.1895г", ex.getMessage());
    }

    @Test
    void shouldAddWhenAddValidFilmReleaseDateBoundary() {
        film.setId(2);
        film.setName("Correct Name");
        film.setDescription("Correct description.");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        filmService.createFilm(film);
        assertEquals(2, film.getId());
    }

    @Test
    void shouldThrowExceptionWhenAddFailedFilmDescription() {
        film.setId(new Random().nextInt(50));
        film.setName("Correct Name");
        film.setDescription("Failed description. Failed description. Failed description. Failed description. " +
                "Failed description. Failed description. Failed description. Failed description. " +
                "Failed description. Failed description. F");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100);
        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> filmService.createFilm(film));
        assertEquals("Длина описания превышает 200 символов", ex.getMessage());
    }

    @Test
    void shouldAddWhenAddFilmDescriptionBoundary() {
        film.setId(new Random().nextInt(50));
        film.setName("Correct Name");
        film.setDescription("Correct description. Correct description. Correct description. Correct description. " +
                "Correct description. Correct description. Correct description. Correct description. " +
                "Correct description. Correct des");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100);
        filmService.createFilm(film);
        assertNotEquals(0, film.getId());
    }

    @Test
    void shouldThrowExceptionWhenUpdateFailedFilmId() {
        film.setId(999);
        film.setName("Correct Name");
        film.setDescription("Correct description.");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100);
        NotFoundException ex = assertThrows(NotFoundException.class, () -> filmService.updateFilm(film));
        assertEquals("Объект с идентификатором 999 не найден", ex.getMessage());
    }
}
