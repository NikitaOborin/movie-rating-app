package com.project.movieratingapp.controller;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.service.FilmService;
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
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> getFilms() {
        log.info("FilmController: getFilms(): start");
        return filmService.getFilms();
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("FilmController: addFilm(): start with film={}", film);
        return filmService.addFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("FilmController: updateFilm(): start with film={}", film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("FilmController: getFilmById(): start with id={}", id);
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("FilmController: addLike(): start with id={}, userId={}", id, userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("FilmController: deleteLike(): start with id={}, userId={}", id, userId);
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("FilmController: getMostPopularFilms(): start with count={}", count);
        return filmService.getMostPopularFilms(count);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirectorId(@PathVariable Integer directorId, @RequestParam String sortBy) {
        log.info("FilmController: getFilmsByDirectorId(): start with directorId={}", directorId);
        return filmService.getFilmsByDirectorId(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> getFilmsWithSubstring(@RequestParam(required = false) String query,
                                            @RequestParam(required = false) String by) {
        log.info("FilmController: getFilmsWithSubstring(): start");
        return filmService.getFilmsWithSubstring(query, by);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilmsWithFriend(@RequestParam Long userId, @RequestParam Long friendId) {
        log.info("FilmController: getFilmsByDirectorId(): start with userId={}, friendId={}", userId, friendId);
        return filmService.getCommonFilmsWithFriend(userId, friendId);
    }
}