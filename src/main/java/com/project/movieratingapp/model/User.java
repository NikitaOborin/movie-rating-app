package com.project.movieratingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}
