package com.project.movieratingapp.repository;

import com.project.movieratingapp.model.Film;

import java.util.List;

public interface FilmRepository {
    public Film addFilm(Film film);
    public Film updateFilm(Film film);
    public List<Film> getFilms();
}