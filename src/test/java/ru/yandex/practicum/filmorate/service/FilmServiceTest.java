package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.AssertionsForClassTypes;

import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmServiceTest {
    private  final FilmService filmService;
    private  final FilmDbStorage filmStorage;
    private  final UserDbStorage userStorage;
    private static Film firstFilm;
    private static Film secondFilm;
    private static Film thirdFilm;
    private static User firstUser;
    private static User secondUser;
    private static User thirdUser;

    @BeforeEach
    public void beforeEach() {
        firstUser = User.builder()
                .name("first")
                .login("First")
                .email("1@ya.ru")
                .birthday(LocalDate.of(1980, 12, 23))
                .build();

        secondUser = User.builder()
                .name("Second")
                .login("Second")
                .email("2@ya.ru")
                .birthday(LocalDate.of(1980, 12, 24))
                .build();

        thirdUser = User.builder()
                .name("Third")
                .login("Third")
                .email("3@ya.ru")
                .birthday(LocalDate.of(1980, 12, 25))
                .build();

        firstFilm = Film.builder()
                .name("film 1")
                .description("description 1")
                .releaseDate(LocalDate.of(1961, 10, 5))
                .duration(114)
                .build();
        firstFilm.setMpa(new Rating(1, "G", "Фильм демонстрируется без ограничений"));
        firstFilm.setLikes(new HashSet<>());
        firstFilm.setGenres(new HashSet<>(Arrays.asList(new Genre(2, "Драма"),
                new Genre(1, "Комедия"))));

        secondFilm = Film.builder()
                .name("film 2")
                .description("description 2")
                .releaseDate(LocalDate.of(2009, 12, 10))
                .duration(162)
                .build();
        secondFilm.setMpa(new Rating(3, "PG-13", "Просмотр не желателен детям до 13 лет"));
        secondFilm.setLikes(new HashSet<>());
        secondFilm.setGenres(new HashSet<>(List.of(new Genre(6, "Боевик"))));

        thirdFilm = Film.builder()
                .name("film 3")
                .description("description 3")
                .releaseDate(LocalDate.of(1975, 11, 19))
                .duration(133)
                .build();
        thirdFilm.setMpa(new Rating(4, "R", "Лица, не достигшие 17-летнего возраста, допускаются на фильм только в сопровождении одного из родителей, либо законного представителя"));
        thirdFilm.setLikes(new HashSet<>());
        thirdFilm.setGenres(new HashSet<>(List.of(new Genre(2, "Драма"))));
    }

    @Test
    public void testCreateFilmAndGetFilmById() {
        firstFilm = filmStorage.createFilm(firstFilm);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(firstFilm.getId()));
        assertThat(filmOptional)
                .hasValueSatisfying(film -> AssertionsForClassTypes.assertThat(film)
                        .hasFieldOrPropertyWithValue("id", firstFilm.getId())
                        .hasFieldOrPropertyWithValue("name", "film 1")
                );
    }

    @Test
    public void testGetFilms() {
        firstFilm = filmStorage.createFilm(firstFilm);
        secondFilm = filmStorage.createFilm(secondFilm);
        thirdFilm = filmStorage.createFilm(thirdFilm);
        List<Film> listFilms = filmStorage.getFilms();
        assertThat(listFilms).contains(firstFilm);
        assertThat(listFilms).contains(secondFilm);
        assertThat(listFilms).contains(thirdFilm);
    }

    @Test
    public void testUpdateFilm() {
        firstFilm = filmStorage.createFilm(firstFilm);
        Film updateFilm = Film.builder()
                .id(firstFilm.getId())
                .name("UpdateName")
                .description("UpdateDescription")
                .releaseDate(LocalDate.of(1975, 11, 19))
                .duration(133)
                .build();
        updateFilm.setMpa(new Rating(1, "G","Фильм демонстрируется без ограничений"));
        Optional<Film> testUpdateFilm = Optional.ofNullable(filmStorage.updateFilm(updateFilm));
        assertThat(testUpdateFilm)
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "UpdateName")
                                .hasFieldOrPropertyWithValue("description", "UpdateDescription")
                );
    }

    @Test
    public void deleteFilm() {
        firstFilm = filmStorage.createFilm(firstFilm);
        secondFilm = filmStorage.createFilm(secondFilm);
        filmStorage.deleteFilm(firstFilm.getId());
        List<Film> listFilms = filmStorage.getFilms();
        assertThat(listFilms).hasSize(1);
        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "film 2"));
    }

    @Test
    public void testAddLike() {
        firstUser = userStorage.createUser(firstUser);
        firstFilm = filmStorage.createFilm(firstFilm);
        filmService.addLike(firstFilm.getId(), firstUser.getId());
        firstFilm = filmStorage.getFilmById(firstFilm.getId());
        assertThat(firstFilm.getLikes()).hasSize(1);
        assertThat(firstFilm.getLikes()).contains(firstUser.getId());
    }

    @Test
    public void testDeleteLike() {
        firstUser = userStorage.createUser(firstUser);
        secondUser = userStorage.createUser(secondUser);
        firstFilm = filmStorage.createFilm(firstFilm);
        filmService.addLike(firstFilm.getId(), firstUser.getId());
        filmService.addLike(firstFilm.getId(), secondUser.getId());
        filmService.deleteLike(firstFilm.getId(), firstUser.getId());
        firstFilm = filmStorage.getFilmById(firstFilm.getId());
        assertThat(firstFilm.getLikes()).hasSize(1);
        assertThat(firstFilm.getLikes()).contains(secondUser.getId());
    }

    @Test
    public void testGetPopularFilms() {

        firstUser = userStorage.createUser(firstUser);
        secondUser = userStorage.createUser(secondUser);
        thirdUser = userStorage.createUser(thirdUser);

        firstFilm = filmStorage.createFilm(firstFilm);
        filmService.addLike(firstFilm.getId(), firstUser.getId());

        secondFilm = filmStorage.createFilm(secondFilm);
        filmService.addLike(secondFilm.getId(), firstUser.getId());
        filmService.addLike(secondFilm.getId(), secondUser.getId());
        filmService.addLike(secondFilm.getId(), thirdUser.getId());

        thirdFilm = filmStorage.createFilm(thirdFilm);
        filmService.addLike(thirdFilm.getId(), firstUser.getId());
        filmService.addLike(thirdFilm.getId(), secondUser.getId());

        List<Film> listFilms = filmService.getPopularFilms(5);

        assertThat(listFilms).hasSize(3);

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "film 2"));

        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "film 3"));

        assertThat(Optional.of(listFilms.get(2)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "film 1"));
    }
}
