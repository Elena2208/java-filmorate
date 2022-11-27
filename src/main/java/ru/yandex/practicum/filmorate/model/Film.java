package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    @PositiveOrZero
    private int id;
    @NotBlank(message = "Отсутствует название фильма")
    private final String name;
    @NotNull(message = "Отсутствует описание фильма")
    @Size(max = 200, message = "Описание фильма не более 200 знаков")
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @Min(value = 1, message = "Продолжительность фильма положительное значение")
    @Positive
    private final long duration;
    private Set<Integer> usersLikes = new HashSet<>();
    private List<Integer> ganres = new ArrayList<>();
    private Integer ratingMPA;
}
