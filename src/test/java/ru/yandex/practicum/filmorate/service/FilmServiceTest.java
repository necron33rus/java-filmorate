package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmServiceTest {
    private static FilmService filmService;
    private static final FilmStorage filmStorage = new InMemoryFilmStorage();
    private static final UserStorage userStorage = new InMemoryUserStorage();
    private static Film film;
    private static Film secondFilm;
    private static User user;
    private static User secondUser;
    private static User thirdUser;

    @BeforeAll
    public static void beforeAll() {
        film = new Film();
        secondFilm = new Film();
        user = new User();
        secondUser = new User();
        thirdUser = new User();
        filmService = new FilmService(filmStorage, userStorage);
    }

    @Test
    void shouldAddWhenAddValidFilmData() {
        film.setId(666L);
        film.setName("Correct Name");
        film.setDescription("Correct description.");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100);
        filmStorage.createFilm(film);
        assertEquals(1, film.getId());
    }

    @Test
    void shouldAddWhenAddValidFilmReleaseDateBoundary() {
        film.setId(333L);
        film.setName("Correct Name");
        film.setDescription("Correct description.");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        filmStorage.createFilm(film);
        assertEquals(2, film.getId());
    }

    @Test
    void shouldAddWhenAddFilmDescriptionBoundary() {
        film.setId(444L);
        film.setName("Correct Name");
        film.setDescription("Correct description. Correct description. Correct description. Correct description. " +
                "Correct description. Correct description. Correct description. Correct description. " +
                "Correct description. Correct des");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100);
        filmStorage.createFilm(film);
        assertNotEquals(0, film.getId());
    }

    @Test
    void shouldThrowExceptionWhenUpdateFailedFilmId() {
        film.setId(999L);
        film.setName("Correct Name");
        film.setDescription("Correct description.");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100);
        NotFoundException ex = assertThrows(NotFoundException.class, () -> filmStorage.updateFilm(film));
        assertEquals("InMemoryFilmStorage: Объект с идентификатором 999 не найден", ex.getMessage());
    }

    @Test
    void shouldAddLike() {
        user.setId(666L);
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");
        userStorage.createUser(user);

        film.setId(999L);
        film.setName("Correct Name");
        film.setDescription("Correct description.");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100);
        filmStorage.createFilm(film);

        filmService.addLike(film.getId(), user.getId());

        assertEquals(1, film.getLikes().size());
    }

    @Test
    void shouldDeleteLike() {
        user.setId(666L);
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");
        userStorage.createUser(user);

        film.setId(999L);
        film.setName("Correct Name");
        film.setDescription("Correct description.");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100);
        filmStorage.createFilm(film);

        filmService.addLike(film.getId(), user.getId());
        assertEquals(2, film.getLikes().size());
        filmService.deleteLike(film.getId(), user.getId());
        assertEquals(1, film.getLikes().size());
    }

    @Test
    void shouldGetPopularFilms() {
        user.setId(666L);
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");
        secondUser.setId(667L);
        secondUser.setName("Correct Name");
        secondUser.setBirthday(LocalDate.of(2002, 1, 1));
        secondUser.setLogin("correctlogin");
        secondUser.setEmail("correct.email@mail.ru");
        thirdUser.setId(668L);
        thirdUser.setName("Correct Name");
        thirdUser.setBirthday(LocalDate.of(2002, 1, 1));
        thirdUser.setLogin("correctlogin");
        thirdUser.setEmail("correct.email@mail.ru");

        userStorage.createUser(user);
        userStorage.createUser(secondUser);
        userStorage.createUser(thirdUser);

        film.setId(999L);
        film.setName("Correct Name");
        film.setDescription("Correct description.");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100);
        secondFilm.setId(999L);
        secondFilm.setName("Correct Name");
        secondFilm.setDescription("Correct description.");
        secondFilm.setReleaseDate(LocalDate.of(1995, 5, 26));
        secondFilm.setDuration(100);

        filmStorage.createFilm(film);
        filmStorage.createFilm(secondFilm);

        filmService.addLike(film.getId(), user.getId());
        filmService.addLike(film.getId(), secondUser.getId());
        filmService.addLike(film.getId(), thirdUser.getId());
        filmService.addLike(secondFilm.getId(), user.getId());
        filmService.addLike(secondFilm.getId(), secondUser.getId());

        filmService.getPopularFilms(10);
        assertEquals(7, filmService.getPopularFilms(10).size());
    }
}
