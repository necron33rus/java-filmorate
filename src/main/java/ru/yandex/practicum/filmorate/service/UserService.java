package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AleradyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final Map<Integer, User> users = new HashMap<>();
    private int currentId;

    public Collection<User> getAllUsers() {
        log.debug("UserService: Массив объектов передан в ответ на запрос");
        return List.copyOf(users.values());
    }

    public void createUser(User user) {
        validate(user);
        if (users.containsKey(user.getId())) {
            log.warn("UserService: Объект с идентификатором {} был уже создан", user.getId());
            throw new AleradyExistException("Объект с идентификатором " + user.getId() + " уже был создан");
        } else {
            user.setId(++currentId);
            users.put(user.getId(), user);
            log.debug("UserService: Создан объект {} с идентификатором {}", user, user.getId());
        }
    }

    public void updateUser(User user) {
        validate(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("UserService: Объект с идентификатором {} обновлен", user.getId());
        } else {
            log.debug("UserService: Объект с идентификатором {} не найден", user.getId());
            throw new NotFoundException("Объект с идентификатором " + user.getId() + " не найден");
        }
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("UserService: Поле name не задано. Значение {} заменено на {} из поля login", user.getName(),
                    user.getLogin());
        }
    }
}
