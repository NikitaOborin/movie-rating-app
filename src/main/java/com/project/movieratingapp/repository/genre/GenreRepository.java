package com.project.movieratingapp.repository.genre;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.Genre;

import java.util.List;

public interface GenreRepository {
    List<Genre> getListGenre();

    Genre getGenreById(Integer genreId);

    List<Genre> getGenreByFilmId(Long film_id);

    void updateGenreInDbForFilm(Film film);

    void deleteGenreByFilm(Film film);

    List<Genre> getListGenresWithoutDuplicate(List<Genre> genres);
}
