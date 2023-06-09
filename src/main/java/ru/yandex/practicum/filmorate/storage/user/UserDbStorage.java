package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
        if (userId == null) {
            throw new ValidationException("UserDbStorage: Передан пустой аргумент");
        }
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
        if (user == null) {
            throw new ValidationException("UserDbStorage: Передан пустой аргумент");
        }
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
        if (userId == null) {
            throw new ValidationException("UserDbStorage: Передан пустой аргумент");
        }
        String sqlString = "DELETE FROM USERS WHERE ID = ?";
        if (jdbcTemplate.update(sqlString, userId) == 0) {
            throw new NotFoundException("UserDbStorage: Фильм с ID=" + userId + " не найден!");
        }
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
}
