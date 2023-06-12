package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public void addFriend(Long userId, Long friendId) {
        if (userStorage.isUserExist(userId) && userStorage.isUserExist(friendId)) {
            if (!Objects.equals(userId, friendId)) {
                friendsStorage.addFriendship(userId, friendId);
                log.info("UserService: Пользователю с идентификатором {} отправил запрос на добавление в друзья " +
                        "Пользователя с идентификатором {}", userId, friendId);
            } else {
                throw new ValidationException("UserService: Нельзя добавить в друзья самого себя");
            }
        } else {
            throw new NotFoundException("UserService: Пользователь с идентификатором " + userId + " не найден");
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (userStorage.isUserExist(userId) && userStorage.isUserExist(friendId)) {
            friendsStorage.deleteFriendship(userId, friendId);
            log.info("UserService: У Пользователя с идентификатором {} из друзей удален " +
                    "Пользователь с идентификатором {}", userId, friendId);
        } else {
            throw new NotFoundException("UserService: Пользователь с идентификатором " + userId + " не найден");
        }
    }

    public List<User> getFriends(Long userId) {
        List<User> friends;
        if (userStorage.isUserExist(userId)) {
            friends = friendsStorage.getFriends(userId);
            return friends;
        } else {
            throw new NotFoundException("UserService: Пользователь с идентификатором " + userId + " не найден");
        }
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        Set<User> friendsIntersection = null;
        if (userStorage.isUserExist(userId) && userStorage.isUserExist(otherUserId)) {
            friendsIntersection = new HashSet<>(friendsStorage.getFriends(userId));
            friendsIntersection.retainAll(friendsStorage.getFriends(otherUserId));
        }
        return new ArrayList<User>(friendsIntersection);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long userId) {
        if (userStorage.isUserExist(userId)) {
            return userStorage.getUserById(userId);
        } else {
            throw new NotFoundException("UserService: Пользователь с идентификатором " + userId + " не найден");
        }

    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        if (userStorage.isUserExist(user.getId())) {
            return userStorage.updateUser(user);
        } else {
            throw new NotFoundException("UserService: Пользователь с идентификатором " + user.getId() + " не найден");
        }
    }

    public void deleteUser(Long userId) {
        if (userStorage.isUserExist(userId)) {
            userStorage.deleteUser(userId);
        } else {
            throw new NotFoundException("UserService: Пользователь с идентификатором " + userId + " не найден");
        }
    }

    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }
}
