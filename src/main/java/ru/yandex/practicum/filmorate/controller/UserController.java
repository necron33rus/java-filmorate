package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("UserController: Получен GET запрос к эндпоинту /users");
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("UserController: Получен POST запрос к эндпоинту /users. Тело запроса: {}", user);
        userService.createUser(user);
        return user;
    }

    @PutMapping
    public User updateOrCreateUser(@Valid @RequestBody User user) {
        log.info("UserController: Получен PUT запрос к эндпоинту /users. Тело запроса: {}", user);
        userService.updateUser(user);
        return user;
    }
}
