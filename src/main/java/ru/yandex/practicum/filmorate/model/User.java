package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private final int id;
    @NotEmpty(message ="Email cannot be empty." )
    @Email(message = "Invalid email format.")
    private final String email;
    @NotEmpty(message ="Login cannot be empty."  )
    private final String login;
    private final String name;
    @Past
    private final LocalDate birthday;
}
