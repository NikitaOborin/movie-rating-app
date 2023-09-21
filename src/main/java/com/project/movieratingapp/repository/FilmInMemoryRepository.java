package com.project.movieratingapp.repository;

import com.project.movieratingapp.exception.NotFoundException;
import com.project.movieratingapp.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class FilmInMemoryRepository implements FilmRepository {
    private final Map<Long, Film> films = new HashMap<>();
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
            throw new NotFoundException(film + " not found");
        }
    }

    @Override
    public List<Film> getFilms() {
        log.info("getFilms repository: start");
        return new ArrayList<>(films.values());
    }
}