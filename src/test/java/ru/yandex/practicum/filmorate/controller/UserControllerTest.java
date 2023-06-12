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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private static Gson gson;
    private static User firstUser;
    private static User secondUser;
    private static User thirdUser;
    private static User forthUser;

    @BeforeAll
    public static void beforeAll() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    @BeforeEach
    public void beforeEach() {
        firstUser = User.builder()
                .id(5L)
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

        forthUser = User.builder()
                .id(4L)
                .name("Third")
                .login("Third")
                .email("3@ya.ru")
                .birthday(LocalDate.of(1980, 12, 25))
                .build();
    }

    @AfterEach
    public void afterEach() throws Exception {
        mockMvc.perform(delete("/users/"));
    }

    @Test
    public void shouldCreateValidUsers() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isOk());

        firstUser.setEmail("incorrect.emailmail.ru");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isBadRequest());

        firstUser.setLogin("   ");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isBadRequest());

        firstUser.setLogin("incorrect login");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateValidUser() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isOk());

        firstUser.setLogin("incorrect login");
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isBadRequest());

        firstUser.setLogin("   ");
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isBadRequest());

        firstUser.setEmail("incorrect.emailmail.ru");
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetAllUsers() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(secondUser)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAddFriend() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(secondUser)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users/3/friends/4"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteFriend() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(secondUser)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users/12/friends/13"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/users/12/friends/13"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetUserById() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetAllFriendsByUserId() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(secondUser)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users/14/friends/15"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/14/friends"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetCommonFriends() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(firstUser)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(secondUser)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(thirdUser)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(forthUser)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users/8/friends/9"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users/8/friends/10"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users/9/friends/10"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/8/friends/common/9"))
                .andExpect(status().isOk());
    }

}