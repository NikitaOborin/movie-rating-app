package com.project.movieratingapp.exception;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(NotFoundException e) {
        log.info("ErrorHandler: handleNotFoundException(): start with message: {}", e.getLocalizedMessage());
        return Map.of("message", e.getLocalizedMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(ValidationException e) {
        log.info("ErrorHandler: handleValidationException(): start with message: {}", e.getLocalizedMessage());
        return Map.of("message", e.getLocalizedMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(RuntimeException e) {
        log.info("ErrorHandler: handleException(): start with message: {}", e.getLocalizedMessage());
        return Map.of("message", e.getLocalizedMessage());
    }
}