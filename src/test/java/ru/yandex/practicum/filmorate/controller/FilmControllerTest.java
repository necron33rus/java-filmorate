package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.LocalDateAdapter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private static Gson gson;
    private static Film firstFilm;
    private static User firstUser;
    private static User secondUser;
    private static User thirdUser;
    private static Film secondFilm;
    private static Film thirdFilm;

    @BeforeAll
    public static void beforeAll() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        firstUser = User.builder()
                .id(1L)
                .name("first")
                .login("First")
                .email("1@ya.ru")
                .birthday(LocalDate.of(1980, 12, 23))
                .build();

        secondUser = User.builder()
                .id(2L)
                .name("Second")
                .login("Second")
                .email("2@ya.ru")
                .birthday(LocalDate.of(1980, 12, 24))
                .build();

        thirdUser = User.builder()
                .id(3L)
                .name("Third")
                .login("Third")
                .email("3@ya.ru")
                .birthday(LocalDate.of(1980, 12, 25))
                .build();

        firstFilm = Film.builder()
                .id(1L)
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
                .id(2L)
                .name("film 2")
                .description("description 2")
                .releaseDate(LocalDate.of(2009, 12, 10))
                .duration(162)
                .build();
        secondFilm.setMpa(new Rating(3, "PG-13", "Просмотр не желателен детям до 13 лет"));
        secondFilm.setLikes(new HashSet<>());
        secondFilm.setGenres(new HashSet<>(List.of(new Genre(6, "Боевик"))));

        thirdFilm = Film.builder()
                .id(3L)
                .name("film 3")
                .description("description 3")
                .releaseDate(LocalDate.of(1975, 11, 19))
                .duration(133)
                .build();
        thirdFilm.setMpa(new Rating(4, "R", "Лица, не достигшие 17-летнего возраста, допускаются на фильм только в сопровождении одного из родителей, либо законного представителя"));
        thirdFilm.setLikes(new HashSet<>());
        thirdFilm.setGenres(new HashSet<>(List.of(new Genre(2, "Драма"))));
    }

    @AfterEach
    public void afterEach() throws Exception {
        mockMvc.perform(delete("/films/"));
        mockMvc.perform(delete("/users/"));
    }

    @Test
    public void shouldCreateValidFilm() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstFilm)))
                .andExpect(status().isOk());

        firstFilm.setDuration(-100);
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstFilm)))
                .andExpect(status().isBadRequest());

        firstFilm.setName("");
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstFilm)))
                .andExpect(status().isBadRequest());

        firstFilm.setName("  ");
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstFilm)))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/films/"));
    }

    @Test
    public void shouldUpdateValidFilm() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstFilm)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstFilm)))
                .andExpect(status().isOk());

        firstFilm.setDuration(-100);
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstFilm)))
                .andExpect(status().isBadRequest());

        firstFilm.setName("");
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstFilm)))
                .andExpect(status().isBadRequest());

        firstFilm.setName("  ");
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstFilm)))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/films/"));
    }

    @Test
    public void shouldGetAllFilms() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(secondFilm)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(secondFilm)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetFilmById() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstFilm)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/films/9"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/films/"));
        mockMvc.perform(delete("/users/"));
    }

    @Test
    public void shouldAddLike() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstFilm)))
                .andExpect(status().isOk());
        mockMvc.perform(put("/films/2/like/16"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/films/"));
        mockMvc.perform(delete("/users/"));
    }

    @Test
    public void shouldDeleteLike() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstFilm)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/films/7/like/20"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/films/7/like/20"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/films/"));
        mockMvc.perform(delete("/users/"));
    }

    @Test
    public void shouldGetPopularFilm() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(secondUser)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(thirdUser)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstFilm)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(secondFilm)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/films/5/like/17"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/films/5/like/18"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/films/5/like/19"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/films/6/like/18"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/films/6/like/19"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/films/"));
        mockMvc.perform(delete("/users/"));
    }
}
