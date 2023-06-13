package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.List;

@Component("userDbStorage")
@Slf4j
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAllUsers() {
        String sqlString = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlString, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate(),
                null)
        );
    }

    @Override
    public User getUserById(Long userId) {
        User user;
        String sqlString = "SELECT * FROM USERS WHERE ID = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sqlString, userId);
        if (rows.first()) {
            user = new User(
                    rows.getLong("id"),
                    rows.getString("email"),
                    rows.getString("login"),
                    rows.getString("name"),
                    rows.getDate("birthday").toLocalDate(),
                    null
            );
        } else {
            throw new NotFoundException("UserDbStorage: Пользователь с ID=" + userId + " не найден!");
        }
        return user;
    }

    @Override
    public User createUser(User user) {
        validate(user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("ID");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        log.info("UserDbStorage: Добавлен новый пользователь с ID = {}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        validate(user);
        if (getUserById(user.getId()) != null) {
            String sqlString = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE ID = ?";
            jdbcTemplate.update(sqlString,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            log.info("UserDbStorage: Пользователь с ID = {} успешно обновлен", user.getId());
            return user;
        } else {
            throw new NotFoundException("UserDbStorage: Пользователь с ID=" + user.getId() + " не найден!");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        String sqlString = "DELETE FROM USERS WHERE ID = ?";
        jdbcTemplate.update(sqlString, userId);
        log.info("UserDbStorage: Пользователь с ID = {} успешно удален", userId);
    }

    @Override
    public void validate(User user) {
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
            log.debug("UserDbStorage: Поле name не задано. Значение {} заменено на {} из поля login", user.getName(),
                    user.getLogin());
        }
    }

    public boolean isUserExist(Long userId) {
        if (getAllUsers().contains(getUserById(userId))) {
            log.info("Валидация: Пользователь с идентификатором {} найден", userId);
        } else {
            throw new ValidationException("Валидация: Пользователь с идентификатором " + userId + " не найден");
        }
        return true;
    }

    @Override
    public void deleteAllUsers() {
        String sqlString = "DELETE FROM USERS";
        jdbcTemplate.update(sqlString);
        log.info("UserDbStorage: Все пользователи успешно удалены");
    }
}
