package ru.yandex.practicum.filmorate.storage.friends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendshipStatuses;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ValidationException;
import java.util.List;

@Component
public class FriendsStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    @Autowired
    public FriendsStorage(JdbcTemplate jdbcTemplate, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    public void addFriendship(Long userId, Long friendId) {
        if (userId == null || friendId == null) {
            throw new ValidationException("FriendStorage: Передан пустой аргумент");
        }
        String sqlString = "INSERT INTO REF_FRIENDSHIP (USER_ID, FRIEND_ID, FRIENDSHIP_STATUS_ID) VALUES (?, ?, ?)";
        if (userStorage.getUserById(userId) != null && userStorage.getUserById(friendId) != null) {
            jdbcTemplate.update(sqlString, userId, friendId, FriendshipStatuses.getIdByEnumName(FriendshipStatuses.REQESTED));
        }
    }

    public void deleteFriendship(Long userId, Long friendId) {
        if (userId == null && friendId == null) {
            throw new ValidationException("FriendStorage: Передан пустой аргумент");
        } else if (friendId == null) {
            String sqlString = "DELETE FROM REF_FRIENDSHIP WHERE USER_ID = ?";
            jdbcTemplate.update(sqlString, userId);
        } else {
            String sqlString = "DELETE FROM REF_FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
            jdbcTemplate.update(sqlString, userId, friendId);
        }
    }

    public List<User> getFriends(Long userId) {
        if (userId == null) {
            throw new ValidationException("FriendStorage: Передан пустой аргумент");
        } else if (userStorage.getUserById(userId) != null) {
            String sqlString = "SELECT ID, EMAIL, LOGIN, NAME, BIRTHDAY FROM USERS " +
                    "JOIN REF_FRIENDSHIP ON REF_FRIENDSHIP.FRIEND_ID = USERS.ID WHERE REF_FRIENDSHIP.USER_ID = ?";
            return jdbcTemplate.query(sqlString, (rs, rowNum) -> new User(
                            rs.getLong("id"),
                            rs.getString("email"),
                            rs.getString("login"),
                            rs.getString("name"),
                            rs.getDate("birthday").toLocalDate(),
                            null),
                    userId
            );
        } else {
            return null;
        }
    }
}
