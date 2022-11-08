package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmControllerTest {

    @Test
    public void createFilmNotNameTest() {
        Film newFilm = new Film(1, "", "discription", LocalDate.now(), 120L);
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Film>> violations = validator.validate(newFilm);
        ConstraintViolation<Film> violation = violations.stream().findFirst().orElseThrow(() -> new RuntimeException(""));
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("The field cannot be empty.", violation.getMessageTemplate());
    }


    @Test
    public void createFilmTest() {
        Film newFilm = new Film(1, "Name", "description", LocalDate.now(), 120L);
        boolean isValid = false;
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Film>> violations = validator.validate(newFilm);
        if (violations.isEmpty()) {
            isValid = true;
        }
        assertTrue(isValid);
    }

    @Test
    public  void createFilmByDescriptionSize200Test() {
        Film newFilm = new Film(1, "Film",
                "*********************************************************************************************" +
                        "************************************************************************************************" +
                        "******************************************************************************************" +
                        "*************************************************************************************" +
                        "********************************************************************************" +
                        "********************************************************************************" +
                        "********************************************************************************" +
                        "*******************************************************************************", LocalDate.now(), 120L);
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Film>> violations = validator.validate(newFilm);
        ConstraintViolation<Film> violation = violations.stream().findFirst().orElseThrow(() -> new RuntimeException(""));
        assertEquals("description", violation.getPropertyPath().toString());
        assertEquals("The description must be less than 200 characters.", violation.getMessageTemplate());
    }

    @Test
    public  void createFilmNotReleaseDateTest() {
        Film newFilm = new Film(1, "Name", "discription", null, 120L);
        Validator validator =Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Film>> violations = validator.validate(newFilm);
        ConstraintViolation<Film> violation = violations.stream().findFirst().orElseThrow(() -> new RuntimeException(""));
        assertEquals("releaseDate", violation.getPropertyPath().toString());
        assertEquals("The date cannot be null.", violation.getMessageTemplate());
    }

    @Test
    public  void createFilmNegativeDurationTest() {
        Film newFilm = new Film(1, "Name", "discription", LocalDate.now(), -120L);
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Film>> violations = validator.validate(newFilm);
        ConstraintViolation<Film> violation = violations.stream().findFirst().orElseThrow(() -> new RuntimeException(""));
        assertEquals("duration", violation.getPropertyPath().toString());
        assertEquals("The duration cannot be negative.", violation.getMessageTemplate());
    }
}
