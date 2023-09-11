package com.project.movieratingapp.validation;

import com.project.movieratingapp.model.Film;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmValidationTests {
    private Film film;

    // Инициализация Validator
    private static final Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void addFilm() {
        film = new Film();
        film.setName("nisi eiusmod");
        film.setDescription("adipisicing");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
    }

    @Test
    void shouldFailedValidationWithEmptyName() {
        film.setName("");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Name is empty");
    }

    @Test
    void shouldFailedValidationWith201LengthDescription() {
        film.setDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod "
                          + "tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, "
                          + "quis nostrud exerci tatio.");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Maximum length of the description is over 200 characters");
    }

    @Test
    void shouldSuccessValidationWith200LengthDescription() {
        film.setDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod "
                + "tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, "
                + "quis nostrud exerci tatio");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size(), "Maximum length of the description is over 200 characters");
    }

    @Test
    void shouldFailedValidationWithReleaseDate27dec1895() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Release date earlier than December 28, 1895");
    }

    @Test
    void shouldSuccessValidationWithReleaseDate28dec1895() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Release date earlier than December 28, 1895");
    }

    @Test
    void shouldFailedValidationWithNegativeDuration() {
        film.setDuration(-1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Duration of the film should be positive");
    }

    @Test
    void shouldFailedValidationWithDuration0() {
        film.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Duration of the film should be positive");
    }


}
