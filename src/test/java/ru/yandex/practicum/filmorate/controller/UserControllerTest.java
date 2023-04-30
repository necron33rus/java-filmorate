package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.LocalDateAdapter;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserController userController;
    private static Gson gson;
    private static User user;
    private static User updatedUser;

    @BeforeAll
    public static void beforeAll() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        user = new User();
        updatedUser = new User();
    }

    @Test
    @Order(1)
    public void shouldCreateValidUsers() throws Exception {
        user.setId(1);
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(gson.toJson(user)));

        user.setEmail("incorrect.emailmail.ru");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isBadRequest());

        user.setLogin("   ");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isBadRequest());

        user.setLogin("incorrect login");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(2)
    public void shouldUpdateValidUser() throws Exception {
        updatedUser.setId(1);
        updatedUser.setName("Correct Name_updated");
        updatedUser.setBirthday(LocalDate.of(2002, 1, 1));
        updatedUser.setLogin("correctlogin");
        updatedUser.setEmail("correct.email@mail.ru");

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(gson.toJson(updatedUser)));

        updatedUser.setLogin("incorrect login");
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(updatedUser)))
                .andExpect(status().isBadRequest());

        updatedUser.setLogin("   ");
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(updatedUser)))
                .andExpect(status().isBadRequest());

        updatedUser.setEmail("incorrect.emailmail.ru");
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(updatedUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    public void shouldGetAllUsers() throws Exception {
        user.setId(2);
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");

        updatedUser.setId(3);
        updatedUser.setName("Correct Name_updated");
        updatedUser.setBirthday(LocalDate.of(2002, 1, 1));
        updatedUser.setLogin("correctLogin");
        updatedUser.setEmail("correct.email@mail.ru");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(gson.toJson(user)));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(gson.toJson(updatedUser)));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(gson.toJson(user))))
                .andExpect(content().string(containsString(gson.toJson(updatedUser))));
    }
}
