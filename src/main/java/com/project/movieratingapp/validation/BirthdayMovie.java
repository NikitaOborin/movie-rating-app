package com.project.movieratingapp.validation;

import  jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BirthdayMovieValidator.class)
@Documented
public @interface BirthdayMovie {
    String message() default "{BirthdayMovie.message}";
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}