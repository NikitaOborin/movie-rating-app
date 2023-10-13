package com.project.movieratingapp.repository.genre;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.Genre;

import java.util.List;

public interface GenreRepository {
    List<Genre> getAllGenre();

    List<Genre> getGenreByFilmId(Long film_id);

    void updateGenreInDbForFilm(Film film);
}
