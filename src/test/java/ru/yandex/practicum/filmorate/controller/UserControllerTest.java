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
    private static Gson gson;
    private static User user;
    private static User friend;
    private static User anotherFriend;
    private static User anotherUser;
    private static User updatedUser;

    @BeforeAll
    public static void beforeAll() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        user = new User();
        friend = new User();
        updatedUser = new User();
        anotherFriend = new User();
        anotherUser = new User();
    }

    @Test
    @Order(1)
    public void shouldCreateValidUsers() throws Exception {
        user.setId(1L);
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isOk());

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
        updatedUser.setId(1L);
        updatedUser.setName("Correct Name_updated");
        updatedUser.setBirthday(LocalDate.of(2002, 1, 1));
        updatedUser.setLogin("correctlogin");
        updatedUser.setEmail("correct.email@mail.ru");

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(updatedUser)))
                .andExpect(status().isOk());

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
        user.setId(2L);
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");

        updatedUser.setId(3L);
        updatedUser.setName("Correct Name_updated");
        updatedUser.setBirthday(LocalDate.of(2002, 1, 1));
        updatedUser.setLogin("correctLogin");
        updatedUser.setEmail("correct.email@mail.ru");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(updatedUser)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(gson.toJson(user))))
                .andExpect(content().string(containsString(gson.toJson(updatedUser))));
    }

    @Test
    @Order(4)
    public void shouldAddFriend() throws Exception {
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");

        friend.setName("friend");
        friend.setBirthday(LocalDate.of(2002, 1, 1));
        friend.setLogin("friend");
        friend.setEmail("friend.email@mail.ru");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(friend)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void shouldDeleteFriend() throws Exception {
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");

        friend.setName("friend");
        friend.setBirthday(LocalDate.of(2002, 1, 1));
        friend.setLogin("friend");
        friend.setEmail("friend.email@mail.ru");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(friend)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/users/1/friends/2"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    public void shouldGetuserById() throws Exception {
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    public void shouldGetAllFriendsByUserId() throws Exception {
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");

        friend.setName("friend");
        friend.setBirthday(LocalDate.of(2002, 1, 1));
        friend.setLogin("friend");
        friend.setEmail("friend.email@mail.ru");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(friend)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/1/friends"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    public void shouldGetCommonFriends() throws Exception {
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");

        anotherUser.setName("Correct Name2");
        anotherUser.setBirthday(LocalDate.of(2002, 1, 1));
        anotherUser.setLogin("correctlogin2");
        anotherUser.setEmail("correct.email2@mail.ru");

        friend.setName("friend");
        friend.setBirthday(LocalDate.of(2002, 1, 1));
        friend.setLogin("friend");
        friend.setEmail("friend.email@mail.ru");

        anotherFriend.setName("friend2");
        anotherFriend.setBirthday(LocalDate.of(2002, 1, 1));
        anotherFriend.setLogin("friend2");
        anotherFriend.setEmail("friend2.email@mail.ru");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(friend)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(anotherUser)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(anotherFriend)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users/1/friends/3"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users/2/friends/3"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/1/friends/common/2"))
                .andExpect(status().isOk());
    }

}
