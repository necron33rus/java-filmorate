package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final UserService userService;
    private final UserDbStorage userStorage;
    private static User firstUser;
    private static User secondUser;
    private static User thirdUser;

    @BeforeEach
    public void beforeEach() {
        firstUser = User.builder()
                .name("First")
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
    }

    @Test
    public void testCreateUserAndGetUserById() {
        userStorage.createUser(firstUser);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(firstUser.getId()));
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", firstUser.getId())
                                .hasFieldOrPropertyWithValue("name", "First"));
    }

    @Test
    public void testGetUsers() {
        userStorage.createUser(firstUser);
        userStorage.createUser(secondUser);
        List<User> listUsers = userStorage.getAllUsers();
        assertThat(listUsers).contains(firstUser);
        assertThat(listUsers).contains(secondUser);
    }

    @Test
    public void testUpdateUser() {
        userStorage.createUser(firstUser);
        User updateUser = User.builder()
                .id(firstUser.getId())
                .name("UpdateFirst")
                .login("First")
                .email("1@ya.ru")
                .birthday(LocalDate.of(1980, 12, 23))
                .build();
        Optional<User> testUpdateUser = Optional.ofNullable(userStorage.updateUser(updateUser));
        assertThat(testUpdateUser)
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("name", "UpdateFirst")
                );
    }

    @Test
    public void deleteUser() {
        userStorage.createUser(firstUser);
        userStorage.deleteUser(firstUser.getId());
        List<User> listUsers = userStorage.getAllUsers();
        assertThat(listUsers).hasSize(0);
    }

    @Test
    public void testAddFriend() {
        userStorage.createUser(firstUser);
        userStorage.createUser(secondUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        assertThat(userService.getFriends(firstUser.getId())).hasSize(1);
        assertThat(userService.getFriends(firstUser.getId())).contains(secondUser);
    }

    @Test
    public void testDeleteFriend() {
        userStorage.createUser(firstUser);
        userStorage.createUser(secondUser);
        userStorage.createUser(thirdUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        userService.deleteFriend(firstUser.getId(), secondUser.getId());
        assertThat(userService.getFriends(firstUser.getId())).hasSize(1);
        assertThat(userService.getFriends(firstUser.getId())).contains(thirdUser);
    }

    @Test
    public void testGetFriends() {
        userStorage.createUser(firstUser);
        userStorage.createUser(secondUser);
        userStorage.createUser(thirdUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        assertThat(userService.getFriends(firstUser.getId())).hasSize(2);
        assertThat(userService.getFriends(firstUser.getId())).contains(secondUser, thirdUser);
    }

    @Test
    public void testGetCommonFriends() {
        userStorage.createUser(firstUser);
        userStorage.createUser(secondUser);
        userStorage.createUser(thirdUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        userService.addFriend(secondUser.getId(), firstUser.getId());
        userService.addFriend(secondUser.getId(), thirdUser.getId());
        assertThat(userService.getCommonFriends(firstUser.getId(), secondUser.getId())).hasSize(1);
        assertThat(userService.getCommonFriends(firstUser.getId(), secondUser.getId()))
                .contains(thirdUser);
    }
}
