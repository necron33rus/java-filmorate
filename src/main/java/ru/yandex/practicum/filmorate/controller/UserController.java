package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        log.info("UserController: Получен GET запрос к эндпоинту /users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        log.info("UserController: Получен GET запрос к эндпоинту /users/{}", id);
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("UserController: Получен GET запрос к эндпоинту /users/{}/friends", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("UserController: Получен GET запрос к эндпоинту /users/{}/friends/common/{}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("UserController: Получен PUT запрос к эндпоинту /users/{}/friend/{}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("UserController: Получен DELETE запрос к эндпоинту /users/{}/friend/{}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("UserController: Получен POST запрос к эндпоинту /users. Тело запроса: {}", user);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateOrCreateUser(@Valid @RequestBody User user) {
        log.info("UserController: Получен PUT запрос к эндпоинту /users. Тело запроса: {}", user);
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Получен DELETE-запрос к эндпоинту /users/{}", id);
        userService.deleteUser(id);
    }

    @DeleteMapping
    public void deleteAllUsers() {
        log.info("Получен DELETE-запрос к эндпоинту /users");
        userService.deleteAllUsers();
    }
}
