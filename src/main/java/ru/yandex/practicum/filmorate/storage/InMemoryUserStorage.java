package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        containsUser(user);
        user.setId(id++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!getUsers().containsKey(user.getId())) {
            throw new NotFoundException("User not found.");
        }
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getById(int id) {
        return users.get(id);
    }

    @Override
    public User deleteById(int id) {
        User user = users.get(id);
        users.remove(id);
        return user;
    }

    public void containsUser(User user) {
        if (findAll().stream().anyMatch(us -> us.getLogin().equals(user.getLogin())
                || us.getEmail().equals(user.getEmail()))) {
            throw new AlreadyExistsException("The user already exists.");
        }
    }
}
