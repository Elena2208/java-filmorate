package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private UserController controller;

    @BeforeEach
    void createController() {
        controller = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    void createUser() {
        User user = new User("ele@yandex.ru", "elena", LocalDate.of(1990, 8, 22));
        controller.create(user);
        assertEquals(1, controller.findAll().size());
    }

    @Test
    void deleteUser() {
        User user = new User("ele@yandex.ru", "elena", LocalDate.of(1990, 8, 22));
        controller.create(user);
        controller.deleteById(1);
        assertEquals(0, controller.findAll().size());
    }

    @Test
    void getById() {
        User user = new User("ele@yandex.ru", "elena", LocalDate.of(1990, 8, 22));
        User userTwo = new User("max@yandex.ru", "max", LocalDate.of(1989, 8, 1));
        controller.create(user);
        controller.create(userTwo);
        assertEquals(userTwo, controller.getById(2));
    }
}