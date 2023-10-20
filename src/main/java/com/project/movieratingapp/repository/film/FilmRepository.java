package com.project.movieratingapp.repository.film;

import com.project.movieratingapp.model.Film;

import java.util.List;

public interface FilmRepository {
    List<Film> getFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(Long filmId);

    void deleteFilmById(Long filmId);

    List<Film> getMostPopularFilms(Integer count);

    List<Film> getFilmsByDirectorIdSortByYear(Integer directorId);

    List<Film> getFilmsByDirectorIdSortByLikes(Integer directorId);

    List<Film> getFilmsWithSubstringInDirector(String query);

    List<Film> getFilmsWithSubstringInTitle(String query);

    List<Film> getFilmsWithSubstringInDirectorAndTitle(String query);

    List<Film> getCommonFilmsWithFriend(Long userId, Long friendId);

    List<Film> getFilmsRecommendation(Long userId);
}