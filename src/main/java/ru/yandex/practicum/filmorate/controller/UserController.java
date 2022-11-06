package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController() {
        this.userService = new UserService();
    }
    @GetMapping
    public Collection<User> findAll() {
        return userService.getAllUsers();
    }
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login cannot contain spaces.");
        }
        log.info("New user object has been created.");
        return userService.create(user);
    }
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login cannot contain spaces.");
        }
        log.info("Updated the user object.");
        return userService.update(user);
    }
}
