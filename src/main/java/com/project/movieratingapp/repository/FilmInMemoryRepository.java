package com.project.movieratingapp.repository;

import com.project.movieratingapp.model.Film;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class FilmInMemoryRepository implements FilmRepository {
    HashMap<Long, Film> films = new HashMap<>();
    private long generatorId;

    private long generateId() {
        return ++generatorId;
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(generateId());
        films.put(generatorId, film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}