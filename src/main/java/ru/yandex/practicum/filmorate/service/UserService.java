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

    public User create(User user)
    {
        String name;
        if (user.getName() == null || user.getName().isBlank()) {name=user.getLogin();}
        else {name=user.getName();}
        User newUser =user.withId(getIdInc()).withName(name);
        users.put(newUser.getId(),newUser);
        return newUser;
    }

    public User update(User user)
    {
        if (!users.containsKey(user.getId())) {
            throw new ResponseException("Пользователь  с id "+user.getId()+" не найден");
        }
        users.put(user.getId(), user);
        return user;
    }
    private int getIdInc(){
        return ++id;
    }

}
