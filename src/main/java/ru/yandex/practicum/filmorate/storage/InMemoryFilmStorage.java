package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        if (containsFilm(film)) {
            throw new AlreadyExistsException("The film already exists.");
        }
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("The movie was not found.");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getById(int id) {
        return films.get(id);
    }

    @Override
    public Film deleteById(int id) {
        Film film = films.get(id);
        films.remove(id);
        return film;
    }

    @Override
    public boolean containsFilm(Film film) {
        return findAll().stream()
                .anyMatch(film1 -> film1.getName().equals(film.getName())
                        && film1.getReleaseDate().equals(film.getReleaseDate()));
    }
}
