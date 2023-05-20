package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("UserService: Пользователь с идентификатором " + userId + " не найден");
        } else {
            user.getFriends().add(friendId);
            friend.getFriends().add(userId);
            log.debug("UserService: К Пользователю с идентификатором {} в друзья добавлен " +
                    "Пользователь с идентификатором {}", userId, friendId);
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("UserService: Пользователь с идентификатором " + userId + " не найден");
        } else {
            user.getFriends().remove(friendId);
            friend.getFriends().add(userId);
            log.debug("UserService: У Пользователя с идентификатором {} из друзей удален " +
                    "Пользователь с идентификатором {}", userId, friendId);
        }
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.getUserById(userId);
        if (user != null) {
            List<User> friends = new ArrayList<>();
            for (Long id : user.getFriends()) {
                friends.add(userStorage.getUserById(id));
            }
            return friends;
        } else {
            throw new NotFoundException("UserService: Пользователь с идентификатором " + userId + " не найден");
        }
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherUserId);
        Set<Long> friendsIntersection = new HashSet<>(user.getFriends());
        friendsIntersection.retainAll(otherUser.getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (Long intersectionId : friendsIntersection) {
            commonFriends.add(userStorage.getUserById(intersectionId));
        }
        return commonFriends;
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
