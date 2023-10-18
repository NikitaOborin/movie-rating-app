package com.project.movieratingapp.repository.director;

import com.project.movieratingapp.model.Director;
import com.project.movieratingapp.model.Film;

import java.util.List;

public interface DirectorRepository {
    List<Director> getDirectors();

    Director addDirector(Director director);

    Director updateDirector(Director director);

    Director getDirectorById(Integer directorId);

    void deleteDirectorById(Integer directorId);

    List<Director> getDirectorsByFilmId(Long filmId);

    void updateDirectorInDbByFilm(Film film);

    void deleteDirectorByFilmId(Long filmId);
}
