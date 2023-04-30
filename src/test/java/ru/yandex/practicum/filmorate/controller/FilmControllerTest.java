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
import ru.yandex.practicum.filmorate.utils.LocalDateAdapter;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FilmController filmController;
    private static Gson gson;
    private static Film film;
    private static Film updatedFilm;

    @BeforeAll
    public static void beforeAll() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        film = new Film();
        updatedFilm = new Film();
    }

    @Test
    @Order(1)
    public void shouldCreateValidFilm() throws Exception {
        film.setId(1);
        film.setName("Correct Name");
        film.setDescription("Correct description");
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        film.setDuration(100);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(film)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(gson.toJson(film)));

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
    @Order(2)
    public void shouldUpdateValidFilm() throws Exception {
        updatedFilm.setId(1);
        updatedFilm.setName("Correct Name_updated");
        updatedFilm.setDescription("Correct description");
        updatedFilm.setReleaseDate(LocalDate.of(1895, 12, 29));
        updatedFilm.setDuration(120);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(updatedFilm)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(gson.toJson(updatedFilm)));

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
    @Order(3)
    public void shouldGetAllFilms() throws Exception {
        film.setId(2);
        film.setName("Correct Name");
        film.setDescription("Correct description");
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        film.setDuration(100);

        updatedFilm.setId(3);
        updatedFilm.setName("Correct Name2");
        updatedFilm.setDescription("Correct description2");
        updatedFilm.setReleaseDate(LocalDate.of(2000, 12, 29));
        updatedFilm.setDuration(120);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(film)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(gson.toJson(film)));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(updatedFilm)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(gson.toJson(updatedFilm)));

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(gson.toJson(film))))
                .andExpect(content().string(containsString(gson.toJson(updatedFilm))));
    }
}
