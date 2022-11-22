package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User getById(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("User not found.");
        }
        return userStorage.getById(id);
    }

    public User deleteById(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("User not found.");
        }
        return userStorage.deleteById(id);
    }

    public List<User> addFriend(int firstId, int secondId) {
        if (!userStorage.getUsers().containsKey(firstId) || !userStorage.getUsers().containsKey(secondId)) {
            throw new NotFoundException("User not found.");
        }
        if (userStorage.getById(firstId).getFriends().contains(secondId)) {
            throw new AlreadyExistsException("Users are already friends");
        }
        userStorage.getById(firstId).getFriends().add(secondId);
        userStorage.getById(secondId).getFriends().add(firstId);
        return Arrays.asList(userStorage.getById(firstId), userStorage.getById(secondId));
    }

    public List<User> deleteFriend(int firstId, int secondId) {
        if (!userStorage.getUsers().containsKey(firstId) || !userStorage.getUsers().containsKey(secondId)) {
            throw new NotFoundException("User not found.");
        }
        if (!userStorage.getById(firstId).getFriends().contains(secondId)) {
            throw new AlreadyExistsException("Users are not friends.");
        }
        userStorage.getById(firstId).getFriends().remove(secondId);
        userStorage.getById(secondId).getFriends().remove(firstId);
        return Arrays.asList(userStorage.getById(firstId), userStorage.getById(secondId));
    }

    public List<User> getFriendsListById(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("User not found.");
        }
        return userStorage.getById(id).getFriends()
                .stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriendsList(int firstId, int secondId) {
        if (!userStorage.getUsers().containsKey(firstId) || !userStorage.getUsers().containsKey(secondId)) {
            throw new NotFoundException("User not found.");
        }
        User user = userStorage.getById(firstId);
        User otherUser = userStorage.getById(secondId);
        return user.getFriends().stream()
                .filter(friendId -> otherUser.getFriends().contains(friendId))
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }
}
