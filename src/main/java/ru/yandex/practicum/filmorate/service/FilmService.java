package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ResponseException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmService {
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    public List<Film> getAllFilm() {
        return new ArrayList<>(films.values());
    }

    public Film createFilm(Film film) {
        Film newFilm = film.withId(getIdInc());
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ResponseException("Фильм с id "+film.getId()+"не найден");
        }
        films.put(film.getId(), film);
        return film;
    }
    private int getIdInc()
    {
        return ++id;
    }
}
