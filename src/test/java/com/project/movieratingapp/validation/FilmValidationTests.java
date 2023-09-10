package com.project.movieratingapp.validation;

import com.project.movieratingapp.model.User;
import jakarta.validation.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmValidationTests {
    // Инициализация Validator
    private static Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void shouldSuccessValidationName() {
        User user = new User();
        user.setName(" ");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Name is empty");
    }
}
