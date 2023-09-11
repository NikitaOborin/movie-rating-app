package com.project.movieratingapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.movieratingapp.validation.BirthdayMovie;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Long id;

    @NotEmpty
    private String name;

    @Size (min = 1, max = 200)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    @BirthdayMovie // кастомная аннотация
    private LocalDate releaseDate;

    @Positive
    private Integer duration;
}