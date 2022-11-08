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
    private int id;
    @NotEmpty(message = "Email cannot be empty.")
    @Email(message = "Invalid email format.")
    private String email;
    @NotEmpty(message = "Login cannot be empty.")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
