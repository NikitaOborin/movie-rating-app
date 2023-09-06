package com.project.movieratingapp.repository;

import com.project.movieratingapp.model.Film;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class FilmRepository {
    HashMap<Long, Film> films = new HashMap<>();
    private long generatorId;

    private long generateId() {
        return ++generatorId;
    }

    public void addFilm(Film film) {
        films.put(generateId(), film);
    }

    public void updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        }
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
