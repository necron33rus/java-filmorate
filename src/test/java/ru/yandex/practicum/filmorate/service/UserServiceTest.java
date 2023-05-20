package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    private static UserService userService;
    private static final UserStorage userStorage = new InMemoryUserStorage();
    private static User user;
    private static User friend;
    private static User otherFriend;

    @BeforeAll
    public static void beforeAll() {
        user = new User();
        friend = new User();
        otherFriend = new User();
        userService = new UserService(userStorage);
    }

    @Test
    void shouldAddUserWhenValidUserData() {
        user.setId(666L);
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");
        userStorage.createUser(user);
        assertTrue(userStorage.getAllUsers().contains(user));
    }

    @Test
    void shouldSetUserNameWhenEmptyUserName() {
        user.setId(555L);
        user.setName("");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");
        userStorage.createUser(user);
        assertNotEquals(0, user.getId());
        assertTrue(userStorage.getAllUsers().contains(user));
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void shouldSetUserNameWhenBlankUserName() {
        user.setId(333L);
        user.setName("   ");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");
        userStorage.createUser(user);
        assertNotEquals(0, user.getId());
        assertTrue(userStorage.getAllUsers().contains(user));
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdateFailedUserId() {
        user.setId(99L);
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");
        NotFoundException ex = assertThrows(NotFoundException.class, () -> userStorage.updateUser(user));
        assertEquals("InMemoryUserStorage: Объект с идентификатором 99 не найден", ex.getMessage());
    }

    @Test
    void shouldAddFriend() {
        user.setId(1L);
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");

        friend.setId(2L);
        friend.setName("Correct Name");
        friend.setBirthday(LocalDate.of(2002, 1, 12));
        friend.setLogin("correctlogin1");
        friend.setEmail("correct.email1@mail.ru");

        userStorage.createUser(user);
        userStorage.createUser(friend);
        userService.addFriend(user.getId(), friend.getId());

        assertEquals(1, user.getFriends().size());
        assertTrue(user.getFriends().contains(friend.getId()));
    }

    @Test
    void shouldAddCommonFriend() {
        user.setId(1L);
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");

        friend.setId(2L);
        friend.setName("Correct Name");
        friend.setBirthday(LocalDate.of(2002, 1, 12));
        friend.setLogin("correctlogin1");
        friend.setEmail("correct.email1@mail.ru");

        otherFriend.setId(3L);
        otherFriend.setName("Correct Name");
        otherFriend.setBirthday(LocalDate.of(2002, 1, 12));
        otherFriend.setLogin("correctlogin1");
        otherFriend.setEmail("correct.email1@mail.ru");

        userStorage.createUser(user);
        userStorage.createUser(friend);
        userStorage.createUser(otherFriend);
        userService.addFriend(user.getId(), friend.getId());
        userService.addFriend(otherFriend.getId(), friend.getId());
        userService.getCommonFriends(user.getId(), otherFriend.getId());

        assertEquals(1, userService.getCommonFriends(user.getId(), otherFriend.getId()).size());
        assertTrue(userService.getCommonFriends(user.getId(), otherFriend.getId()).contains(friend));
    }

    @Test
    void shouldGetFriends() {
        user.setId(1L);
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");

        friend.setId(2L);
        friend.setName("Correct Name");
        friend.setBirthday(LocalDate.of(2002, 1, 12));
        friend.setLogin("correctlogin1");
        friend.setEmail("correct.email1@mail.ru");

        userStorage.createUser(user);
        userStorage.createUser(friend);
        userService.addFriend(user.getId(), friend.getId());

        assertEquals(2, userService.getFriends(user.getId()).size());
        assertTrue(userService.getFriends(user.getId()).contains(friend));
    }

    @Test
    void shouldDeleteFriend() {
        user.setId(1L);
        user.setName("Correct Name");
        user.setBirthday(LocalDate.of(2002, 1, 1));
        user.setLogin("correctlogin");
        user.setEmail("correct.email@mail.ru");

        friend.setId(2L);
        friend.setName("Correct Name");
        friend.setBirthday(LocalDate.of(2002, 1, 12));
        friend.setLogin("correctlogin1");
        friend.setEmail("correct.email1@mail.ru");

        userStorage.createUser(user);
        userStorage.createUser(friend);
        userService.addFriend(user.getId(), friend.getId());

        assertEquals(4, userService.getFriends(user.getId()).size());
        assertTrue(userService.getFriends(user.getId()).contains(friend));

        userService.deleteFriend(user.getId(), friend.getId());
        assertEquals(3, userService.getFriends(user.getId()).size());
    }
}
