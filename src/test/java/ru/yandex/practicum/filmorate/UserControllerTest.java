package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    @Test
    public void createUserTest() {
        boolean isValid = false;
        User user = new User(1, "email@yandex.ru", "elena_1990", "elena",
                LocalDate.of(1990, 8, 22));
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.isEmpty()) {
            isValid = true;
        }
        assertTrue(isValid);
    }

    @Test
    public void createUserByEmptyEmail() {
        User user = new User(1, "", "elena_1990", "elena",
                LocalDate.of(1990, 8, 22));
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        ConstraintViolation<User> violation = violations.stream().findFirst().orElseThrow(() -> new RuntimeException(""));
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("Email cannot be empty.", violation.getMessageTemplate());

    }

    @Test
    public void createUserIncorrectEmailTest() {
        User user = new User(1, "96-*/+-59", "elena_1990", "elena",
                LocalDate.of(1990, 8, 22));
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        ConstraintViolation<User> violation = violations.stream().findFirst().orElseThrow(() -> new RuntimeException(""));
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("Invalid email format.", violation.getMessageTemplate());
    }

    @Test
    public void createUserByEmptyLoginTest() {
        User user = new User(1, "email@yandex.ru", "", "elena",
                LocalDate.of(1990, 8, 22));
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        ConstraintViolation<User> violation = violations.stream().findFirst().orElseThrow(() -> new RuntimeException(""));
        assertEquals("login", violation.getPropertyPath().toString());
        assertEquals("Login cannot be empty.", violation.getMessageTemplate());

    }

    @Test
    public void createUserIncorrectDateTest() {
        boolean isValid = false;
        User user = new User(1, "email@yandex.ru", "elena_1990", "elena",
                LocalDate.of(2023, 8, 22));
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        ConstraintViolation<User> violation = violations.stream().findFirst().orElseThrow(() -> new RuntimeException(""));
        if (user.getBirthday().isBefore(LocalDate.now())) {
            isValid = true;
        }
        assertFalse(isValid);
    }
}
