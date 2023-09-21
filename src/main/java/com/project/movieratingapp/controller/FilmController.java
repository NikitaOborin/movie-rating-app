package com.project.movieratingapp.controller;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.repository.FilmRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmRepository filmRepository;

    @Autowired
    public FilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @GetMapping()
    public List<Film> getFilms() {
        log.info("getFilms controller: start");
        return filmRepository.getFilms();
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("addFilm controller: start with {}", film);
        return filmRepository.addFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("updateFilm controller: start with {}", film);
        return filmRepository.updateFilm(film);
    }
}