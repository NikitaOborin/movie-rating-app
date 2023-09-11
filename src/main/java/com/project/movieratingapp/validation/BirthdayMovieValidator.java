package com.project.movieratingapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class BirthdayMovieValidator implements ConstraintValidator<BirthdayMovie, LocalDate> {
    private final LocalDate birthdayMovie = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate == null) {
            return true;
        }
        return localDate.isAfter(birthdayMovie);
    }
}
