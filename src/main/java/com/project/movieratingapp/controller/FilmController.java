package com.project.movieratingapp.controller;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    @Autowired
    FilmRepository filmRepository;

    @GetMapping()
    public List<Film> getFilms() {
        return filmRepository.getFilms();
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) {
        return filmRepository.addFilm(film);
    }

    @PatchMapping()
    public Film updateFilm(@RequestBody Film film) {
        return filmRepository.updateFilm(film);
    }
}