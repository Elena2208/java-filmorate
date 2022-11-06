package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ResponseException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User create(User user) {
        User newUser = User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
        String name;
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            name = newUser.getLogin();
        } else {
            name = newUser.getName();
        }
        User objectUser = newUser.withId(getIdInc()).withName(name);
        users.put(objectUser.getId(), objectUser);
        return objectUser;
    }
    public User update(User user) {
        User newUser = User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
        if (!users.containsKey(newUser.getId())) {
            throw new ResponseException("Пользователь  с id " + newUser.getId() + " не найден");
        }
        users.put(newUser.getId(), newUser);
        return newUser;
    }
    private int getIdInc() {
        return ++id;
    }
}
