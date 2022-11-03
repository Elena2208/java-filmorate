package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(User.class);
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
        User newUser= User.builder().id(user.getId()).email(user.getEmail()).login(user.getLogin())
                .name(user.getName()).birthday(user.getBirthday()).build();
        if (newUser.getLogin().contains(" ")) {
            throw new ValidationException("Login cannot contain spaces.");
        }
        log.info("New user object has been created.");
        return userService.create(newUser);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {

        User newUser = User.builder().id(user.getId()).email(user.getEmail()).login(user.getLogin())
                .name(user.getName()).birthday(user.getBirthday()).build();

        if (newUser.getLogin().contains(" ")) {
            throw new ValidationException("Login cannot contain spaces.");
        }
        log.info("Updated the user object.");
        return userService.update(newUser);
    }
}
