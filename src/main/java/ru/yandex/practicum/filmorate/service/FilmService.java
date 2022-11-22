package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.*;

import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;


    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;

    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Release date earlier than 28.12.1895.");
        }
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Release date earlier than 28.12.1895.");
        }
        return filmStorage.update(film);
    }

    public Film getById(int id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("The movie was not found.");
        }
        return filmStorage.getById(id);
    }

    public Film deleteById(int id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("The movie was not found.");
        }
        return filmStorage.deleteById(id);
    }

    public Film addLike(int filmId, int userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new NotFoundException("The movie was not found.");
        }
        filmStorage.getById(filmId).getUsersLikes().add(userId);
        return filmStorage.getById(filmId);
    }

    public Film deleteLike(int filmId, int userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new NotFoundException("The movie was not found.");
        }
        if (!filmStorage.getById(filmId).getUsersLikes().contains(userId)) {
            throw new NotFoundException("The user did not like.");
        }
        filmStorage.getById(filmId).getUsersLikes().remove(userId);
        return filmStorage.getById(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.findAll()
                .stream()
                .sorted((o1, o2) -> Integer.compare(o2.getUsersLikes().size(), o1.getUsersLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
