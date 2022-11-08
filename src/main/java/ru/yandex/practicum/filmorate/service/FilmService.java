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
        Film newFilm = Film.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();
        Film objectFilm = newFilm.withId(getIdInc());
        films.put(objectFilm.getId(), objectFilm);
        return objectFilm;
    }

    public Film updateFilm(Film film) {
        Film newFilm = Film.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();

        if (!films.containsKey(newFilm.getId())) {
            throw new ResponseException("Фильм с id " + newFilm.getId() + " не найден");
        }
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    private int getIdInc() {
        return ++id;
    }
}
