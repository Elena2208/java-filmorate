package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;


@Getter
@With
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    int id;
    @NotEmpty(message = "Email cannot be empty.")
    @Email(message = "Invalid email format.")
    String email;
    @NotEmpty(message = "Login cannot be empty.")
    String login;

    String name;
    @Past
    LocalDate birthday;


}
