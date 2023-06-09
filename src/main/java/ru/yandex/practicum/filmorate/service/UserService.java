package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ValidationException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private UserStorage userStorage;
    private FriendsStorage friendsStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        if (Objects.equals(userId, friendId)) {
            throw new ValidationException("UserService: Нельзя добавить в друзья самого себя");
        }
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("UserService: Пользователь с идентификатором " + userId + " не найден");
        } else {
            friendsStorage.addFriendship(userId, friendId);
            log.info("UserService: Пользователю с идентификатором {} отправил запрос на добавление в друзья " +
                    "Пользователя с идентификатором {}", userId, friendId);
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("UserService: Пользователь с идентификатором " + userId + " не найден");
        } else {
            friendsStorage.deleteFriendship(userId, friendId);
            log.info("UserService: У Пользователя с идентификатором {} из друзей удален " +
                    "Пользователь с идентификатором {}", userId, friendId);
        }
    }

    public List<User> getFriends(Long userId) {
        List<User> friends;
        User user = userStorage.getUserById(userId);
        if (user != null) {
            friends = friendsStorage.getFriends(userId);
            return friends;
        } else {
            throw new NotFoundException("UserService: Пользователь с идентификатором " + userId + " не найден");
        }
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherUserId);
        Set<User> friendsIntersection = null;
        if (user != null && otherUser != null) {
            friendsIntersection = new HashSet<>(friendsStorage.getFriends(userId));
            friendsIntersection.retainAll(friendsStorage.getFriends(otherUserId));
        }
        return new ArrayList<User>(friendsIntersection);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }
}
