package com.project.movieratingapp.repository.film;

import com.project.movieratingapp.model.Film;

import java.util.List;

public interface FilmRepository {
    List<Film> getFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(Long filmId);

    List<Film> getMostPopularFilms(Integer count);

    List<Film> getFilmsByDirectorIdSortByYear(Integer directorId);

    List<Film> getFilmsByDirectorIdSortByLikes(Integer directorId);
}