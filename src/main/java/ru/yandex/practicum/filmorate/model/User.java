package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @PositiveOrZero
    private int id;
    @NotBlank(message = "Отсутствует email")
    @Email(message = "Некорректный email")
    @Email
    private final String email;
    @NotNull(message = "Отсутствует логин")
    private final String login;
    private String name;
    @NotNull(message = "Не указана дата рождения")
    @Past(message = "Некорректная дата рождения")
    private final LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();
}
