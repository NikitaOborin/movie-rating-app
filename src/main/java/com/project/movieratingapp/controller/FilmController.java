package com.project.movieratingapp.controller;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.repository.FilmRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    FilmRepository filmRepository;

    @GetMapping()
    public List<Film> getFilms() {
        return filmRepository.getFilms();
    }
}
