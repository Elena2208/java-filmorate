package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre getById(int id) {
        log.info("Запрос был отправлен.");
        return genreStorage.getById(id);
    }

    public List<Genre> getAll() {
        log.info("Запрос был отправлен.");
        return genreStorage.getAll();
    }
}
