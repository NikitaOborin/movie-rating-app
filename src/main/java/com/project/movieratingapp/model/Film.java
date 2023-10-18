package com.project.movieratingapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.movieratingapp.validation.BirthdayMovie;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    private Long id;

    @NotEmpty
    private String name;

    @Size (min = 1, max = 200)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @BirthdayMovie // кастомная аннотация
    private LocalDate releaseDate;

    @Positive
    private Integer duration;

    private Set<Long> likes = new HashSet<>();
    private List<Genre> genres = new ArrayList<>();
    private List<Director> directors = new ArrayList<>();
    private Mpa mpa;
}