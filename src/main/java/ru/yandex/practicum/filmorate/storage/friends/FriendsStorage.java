package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendshipStatuses;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendsStorage {
    private final JdbcTemplate jdbcTemplate;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public void addFriendship(Long userId, Long friendId) {
        if (userStorage.isUserExist(userId) && userStorage.isUserExist(friendId)) {
            String sqlString = "INSERT INTO REF_FRIENDSHIP (USER_ID, FRIEND_ID, FRIENDSHIP_STATUS_ID) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlString, userId, friendId, FriendshipStatuses.getIdByEnumName(FriendshipStatuses.REQESTED));
        }
    }

    public void deleteFriendship(Long userId, Long friendId) {
        if (userStorage.isUserExist(userId) && userStorage.isUserExist(friendId)) {
            String sqlString = "DELETE FROM REF_FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
            jdbcTemplate.update(sqlString, userId, friendId);
        }
    }

    public List<User> getFriends(Long userId) {
        if (userStorage.isUserExist(userId)) {
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
