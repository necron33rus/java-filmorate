package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.AleradyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

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

    @AfterEach
    public void afterEach() {
        userService.deleteAllUsers();
    }

    @Test
    void shouldAddUserWhenValidUserData() throws AleradyExistException {
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");
        userService.createUser(user);
        assertNotEquals(0, user.getId());
        assertTrue(userService.getAllUsers().contains(user));
    }

    @Test
    void shouldSetUserNameWhenEmptyUserName() throws AleradyExistException {
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
    void shouldSetUserNameWhenBlankUserName() throws AleradyExistException {
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
