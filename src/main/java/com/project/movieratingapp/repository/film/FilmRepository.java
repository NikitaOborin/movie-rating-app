package com.project.movieratingapp.repository.film;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.User;

import java.util.List;

public interface FilmRepository {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(Long id);

    Film addLike(Film film, User user);

    Film deleteLike(Film film, User user);
}