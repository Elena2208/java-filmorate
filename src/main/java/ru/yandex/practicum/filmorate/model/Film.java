package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {
    @PositiveOrZero
    private long id;
    @NotBlank(message = "Отсутствует название фильма.")
    private  String name;
    @NotNull(message = "Отсутствует описание фильма.")
    @NotBlank(message = "Описание фильма пустое.")
    @Size(max = 200, message = "Описание фильма не более 200 знаков.")
    private  String description;
    @NotNull
    private  LocalDate releaseDate;
    @Min(value = 1, message = "Продолжительность фильма положительное значение.")
    @Positive
    private long duration;
    private Mpa mpa;
    private Set<Genre> genres;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id && duration == film.duration && Objects.equals(name, film.name) &&
                Objects.equals(description, film.description) && Objects.equals(releaseDate, film.releaseDate)
                && Objects.equals(mpa, film.mpa) && Objects.equals(genres, film.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration, mpa, genres);
    }
}
