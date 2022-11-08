package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@With
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Film {
    private int id;
    @NotEmpty(message = "The field cannot be empty.")
    private String name;
    @Size(max = 200, message = "The description must be less than 200 characters.")
    private String description;
    @NotNull(message = "The date cannot be null.")
    private LocalDate releaseDate;
    @Positive(message = "The duration cannot be negative.")
    private Long duration;
}
