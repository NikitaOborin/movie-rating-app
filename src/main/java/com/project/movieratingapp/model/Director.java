package com.project.movieratingapp.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Director {
    Integer id;

    @NotBlank
    String name;
}
