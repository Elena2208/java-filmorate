package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Validated
@RestController
@RequestMapping("/films")

public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(Film.class);
    private final FilmService filmService;

    public FilmController() {
        this.filmService = new FilmService();
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.getAllFilm();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {

        Film newFilm = Film.builder().id(film.getId()).name(film.getName()).description(film.getDescription())
                .releaseDate(film.getReleaseDate()).duration(film.getDuration()).build();
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Release date earlier than 28.12.1895");
        }
        log.info("New movie object has been created.");

        return  filmService.createFilm(newFilm);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        Film newFilm = Film.builder().id(film.getId()).name(film.getName()).description(film.getDescription())
                .releaseDate(film.getReleaseDate()).duration(film.getDuration()).build();
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Release date earlier than 28.12.1895");
        }
            log.info("Updated the movie object.");

        return filmService.updateFilm(newFilm);
    }
}
