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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.LocalDateAdapter;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private static Gson gson;
    private static Film film;
    private static User user;
    private static User secondUser;
    private static User thirdUser;
    private static Film updatedFilm;
    private static Film secondFilm;

    @BeforeAll
    public static void beforeAll() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        film = new Film();
        secondFilm = new Film();
        user = new User();
        secondUser = new User();
        thirdUser = new User();
        updatedFilm = new Film();
    }

    @BeforeEach
    public void beforeEach() {
        film.setId(1L);
        film.setName("Correct Name");
        film.setDescription("Correct description");
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        film.setDuration(100);

        updatedFilm.setId(1L);
        updatedFilm.setName("Correct Name_updated");
        updatedFilm.setDescription("Correct description");
        updatedFilm.setReleaseDate(LocalDate.of(1895, 12, 29));
        updatedFilm.setDuration(120);

        user.setId(1L);
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

        secondFilm.setId(999L);
        secondFilm.setName("Correct Name");
        secondFilm.setDescription("Correct description.");
        secondFilm.setReleaseDate(LocalDate.of(1995, 5, 26));
        secondFilm.setDuration(100);
    }

    @Test
    public void shouldCreateValidFilm() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(film)))
                .andExpect(status().isOk());

        film.setDuration(-100);
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(film)))
                .andExpect(status().isBadRequest());

        film.setName("");
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(film)))
                .andExpect(status().isBadRequest());

        film.setName("  ");
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateValidFilm() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(film)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(updatedFilm)))
                .andExpect(status().isOk());

        updatedFilm.setDuration(-100);
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(updatedFilm)))
                .andExpect(status().isBadRequest());

        updatedFilm.setName("");
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(updatedFilm)))
                .andExpect(status().isBadRequest());

        updatedFilm.setName("  ");
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(updatedFilm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetAllFilms() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(film)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(updatedFilm)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetFilmById() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(film)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/films/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAddLike() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(film)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/films/1/like/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteLike() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(film)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/films/1/like/1"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/films/1/like/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetPopularFilm() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(secondUser)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(thirdUser)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(film)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(secondFilm)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/films/1/like/1"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/films/1/like/2"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/films/1/like/3"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/films/2/like/1"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/films/2/like/2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk());
    }
}
