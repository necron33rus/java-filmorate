package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();

    User getUserById(Long userId);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long userId);

    void validate(User user);

    boolean isUserExist(Long userId);

    void deleteAllUsers();
}
