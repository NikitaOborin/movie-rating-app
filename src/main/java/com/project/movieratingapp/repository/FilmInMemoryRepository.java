package com.project.movieratingapp.repository;

import com.project.movieratingapp.model.Film;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Repository
public class FilmInMemoryRepository implements FilmRepository {
    HashMap<Long, Film> films = new HashMap<>();
    private long generatorId;

    private long generateId() {
        return ++generatorId;
    }

    @Override
    public Film addFilm(Film film) {
        log.info("addFilm repository: start with {}", film);
        film.setId(generateId());
        films.put(generatorId, film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("updateFilm repository: start with {}", film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("ValidationException");
        }
    }

    @Override
    public List<Film> getFilms() {
        log.info("getFilms repository: start");
        return new ArrayList<>(films.values());
    }
}