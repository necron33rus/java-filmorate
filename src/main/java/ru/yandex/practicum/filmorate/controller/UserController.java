package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AleradyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@Validated
@RestController
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService = new UserService();

    @GetMapping("/users")
        public ResponseEntity<Collection<User>> getAllUsers() {
            log.info("UserController: Получен GET запрос к эндпоинту /users");
            return ResponseEntity.ok(userService.getAllUsers()) ;
    }

    @PostMapping("/users")
        public ResponseEntity<User> createUser (@Valid @RequestBody User user) throws AleradyExistException {
        log.info("UserController: Получен POST запрос к эндпоинту /users. Тело запроса: {}", user);
        userService.createUser(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users")
        public ResponseEntity<User> updateOrCreateUser (@Valid @RequestBody User user) {
        log.info("UserController: Получен PUT запрос к эндпоинту /users. Тело запроса: {}", user);
        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users")
        public ResponseEntity<Collection<User>> deleteAllUsers() {
            log.warn("UserController: Получен DELETE запрос к эндпоинту /users");
            return ResponseEntity.ok(userService.deleteAllUsers());
    }
}
