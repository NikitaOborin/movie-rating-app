package com.project.movieratingapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    @Email
    @NotEmpty
    private String email;
    @NotBlank
    private String login;
    private String name; // может быть пустым, в таком случае использовать логин
    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
    private Set<Long> filmLikes = new HashSet<>();
}