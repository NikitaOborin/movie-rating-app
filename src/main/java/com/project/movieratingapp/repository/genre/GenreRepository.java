package com.project.movieratingapp.repository.genre;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.Genre;

import java.util.List;

public interface GenreRepository {
    List<Genre> getListGenre();

    Genre getGenreByGenreId(Integer genreId);

    List<Genre> getGenreByFilmId(Long filmId);

    void updateGenreInDbByFilm(Film film);

    void deleteGenreByFilmId(Long filmId);
}
