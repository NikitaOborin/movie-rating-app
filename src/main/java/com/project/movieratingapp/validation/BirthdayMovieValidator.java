package com.project.movieratingapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class BirthdayMovieValidator implements ConstraintValidator<BirthdayMovie, LocalDate> {
    private final static LocalDate GLOBAL_MOVIE_DAY = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(GLOBAL_MOVIE_DAY) || localDate.equals(GLOBAL_MOVIE_DAY);
    }
}