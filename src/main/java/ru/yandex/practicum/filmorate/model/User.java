package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @PositiveOrZero
    private long id;
    @NotBlank(message = "Отсутствует email")
    @Email(message = "Некорректный email")
    private  String email;
    @NotNull(message = "Отсутствует логин")
    private  String login;
    private String name;
    @NotNull(message = "Не указана дата рождения")
    @Past(message = "Некорректная дата рождения")
    private  LocalDate birthday;
}
