package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    private static UserService userService;
    private static User user;

    @BeforeAll
    public static void beforeAll() {
        user = new User();
        userService = new UserService();
    }

    @Test
    void shouldAddUserWhenValidUserData() {
        user.setId(1);
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");
        userService.createUser(user);
        assertEquals(1, user.getId());
        assertTrue(userService.getAllUsers().contains(user));
    }

    @Test
    void shouldSetUserNameWhenEmptyUserName() {
        user.setId(new Random().nextInt(50));
        user.setName("");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");
        userService.createUser(user);
        assertNotEquals(0, user.getId());
        assertTrue(userService.getAllUsers().contains(user));
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void shouldSetUserNameWhenBlankUserName() {
        user.setId(new Random().nextInt(50));
        user.setName("   ");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");
        userService.createUser(user);
        assertNotEquals(0, user.getId());
        assertTrue(userService.getAllUsers().contains(user));
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdateFailedUserId() {
        user.setId(99);
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");
        NotFoundException ex = assertThrows(NotFoundException.class, () -> userService.updateUser(user));
        assertEquals("Объект с идентификатором 99 не найден", ex.getMessage());
    }
}
