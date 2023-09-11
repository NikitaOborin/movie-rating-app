package com.project.movieratingapp.validation;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserValidationTests {
    User user;

    // Инициализация Validator
    private static final Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void addUser() {
        user = new User();
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        user.setLogin("dolore");
        user.setEmail("mail@mail.ru");
    }

    @Test
    void shouldFailedValidationWithBlankEmail() {
        user.setEmail("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Email cannot be empty and must contain the character @");
    }

    @Test
    void shouldFailedValidationWithOutDogCharacterEmail() {
        user.setEmail("mailmail.ru");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Email cannot be empty and must contain the character @");
    }

    @Test
    void shouldFailedValidationWithEmptyLogin() {
        user.setLogin("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Login cannot be empty and contain spaces");
    }

    @Test
    void shouldFailedValidationWithBlankLogin() {
        user.setLogin(" ");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Login cannot be empty and contain spaces");
    }

    @Test
    void shouldSuccessValidationWithEmptyName() {
        user.setName("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }

    @Test
    void shouldFailedValidationWithBirthdayInFuture() {
        user.setBirthday(LocalDate.of(3000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Birthday cannot be in the future");
    }
}
