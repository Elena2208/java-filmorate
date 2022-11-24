package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {
    private FilmController controller;

    @BeforeEach
    void createController() {
        controller = new FilmController(new FilmService(new InMemoryFilmStorage()));
    }

    @Test
    void createFilm() {
        Film film = new Film("name", "disko", LocalDate.of(2000, 2, 12), 120L);
        controller.create(film);
        assertEquals(1, controller.findAll().size());
    }

    @Test
    void deleteFilm() {
        Film film = new Film("name", "disko", LocalDate.of(2000, 2, 12), 120L);
        controller.create(film);
        controller.deleteById(1);
        assertEquals(0, controller.findAll().size());
    }

    @Test
    void getById() {
        Film film = new Film("name", "disko", LocalDate.of(2000, 2, 12), 120L);
        controller.create(film);
        assertEquals(film, controller.getById(1));
    }
}