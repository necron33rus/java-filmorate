package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AleradyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserService {
    private final Map<Integer, User> users = new HashMap<>();
    private int increment = 0;

    public Collection<User> getAllUsers() {
        log.debug("UserService: Массив объектов передан в ответ на запрос");
        return users.values();
    }

    public void createUser(User user) throws AleradyExistException {
        validate(user);
        if (users.containsKey(user.getId())) {
            log.warn("UserService: Объект с идентификатором {} был уже создан", user.getId());
            throw new AleradyExistException("Объект с идентификатором " + user.getId() + " уже был создан");
        } else {
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

    public Collection<User> deleteAllUsers() {
        users.clear();
        return users.values();
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("UserService: Поле name не задано. Значение {} заменено на {} из поля login", user.getName(),
                    user.getLogin());
        }
        if (user.getId() == 0) {
            user.setId(++increment);
        }
    }
}
