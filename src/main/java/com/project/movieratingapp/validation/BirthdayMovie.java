package com.project.movieratingapp.validation;

import  jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BirthdayMovieValidator.class)
@Documented
public @interface BirthdayMovie {
    String message() default "Ошибка валидации, дата релиза фильма должна быть не раньше 28 декабря 1895 года";
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}