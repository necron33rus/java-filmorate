package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users;
    private Long currentId;

    public InMemoryUserStorage() {
        this.currentId = 0L;
        this.users = new HashMap<>();
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("InMemoryUserStorage: Массив объектов передан в ответ на запрос");
        return List.copyOf(users.values());
    }

    @Override
    public User getUserById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("InMemoryUserStorage: Объект с идентификатором " + userId + " не найден");
        } else {
            log.debug("InMemoryUserStorage: Объект с идентификатором {} передан в ответ на запрос", userId);
            return users.get(userId);
        }
    }

    @Override
    public User createUser(User user) {
        validate(user);
        user.setId(++currentId);
        users.put(user.getId(), user);
        log.debug("InMemoryUserStorage: Создан объект {} с идентификатором {}", user, user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        validate(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("InMemoryUserStorage: Объект с идентификатором {} обновлен", user.getId());
        } else {
            log.debug("InMemoryUserStorage: Объект с идентификатором {} не найден", user.getId());
            throw new NotFoundException("InMemoryUserStorage: Объект с идентификатором " + user.getId() + " не найден");
        }
        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        throw new UnsupportedOperationException("InMemoryFilmStorage: Метод не поддерживается");
    }

    public void validate(User user) {
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
            log.debug("InMemoryUserStorage: Поле name не задано. Значение {} заменено на {} из поля login", user.getName(),
                    user.getLogin());
        }
    }

    @Override
    public boolean isUserExist(Long userId) {
        throw new UnsupportedOperationException("InMemoryFilmStorage: Метод не поддерживается");
    }

    @Override
    public void deleteAllUsers() {
        throw new UnsupportedOperationException("InMemoryFilmStorage: Метод не поддерживается");
    }
}
