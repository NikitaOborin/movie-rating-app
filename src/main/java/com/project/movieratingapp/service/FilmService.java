package com.project.movieratingapp.service;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.repository.film.FilmRepository;
import com.project.movieratingapp.repository.genre.GenreRepository;
import com.project.movieratingapp.repository.like.LikeRepository;
import com.project.movieratingapp.repository.mpa.MpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class FilmService {
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public FilmService(@Qualifier("filmDBRepository") FilmRepository filmRepository,
                                                      GenreRepository genreRepository,
                                                      MpaRepository mpaRepository,
                                                      LikeRepository likeRepository) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
        this.mpaRepository = mpaRepository;
        this.likeRepository = likeRepository;
    }

    public List<Film> getFilms() {
        log.info("FilmService: getFilms(): start");
        List<Film> films = filmRepository.getFilms();

        for (Film film : films) {
            film.setGenres(genreRepository.getGenreByFilmId(film.getId()));
            film.setMpa(mpaRepository.getMpaByFilmId(film.getId()));
            film.setLikes(likeRepository.getLikesByFilmId(film.getId()));
        }

        return films;
    }

    public Film addFilm(Film film) {
        log.info("FilmService: addFilm(): start with film={}", film);
        Film addedFilm =  filmRepository.addFilm(film);

        genreRepository.updateGenreInDbByFilm(film);

        return addedFilm;
    }

    public Film updateFilm(Film film) {
        log.info("FilmService: updateFilm(): start with film={}", film);
        filmRepository.updateFilm(film);

        genreRepository.updateGenreInDbByFilm(film);

        film.setGenres(genreRepository.getGenreByFilmId(film.getId()));

        return film;
    }

    public Film getFilmById(Long id) {
        log.info("FilmService: getFilmById(): start with id={}", id);
        Film film = filmRepository.getFilmById(id);

        film.setGenres(genreRepository.getGenreByFilmId(film.getId()));
        film.setMpa(mpaRepository.getMpaByFilmId(film.getId()));
        film.setLikes(likeRepository.getLikesByFilmId(film.getId()));

        return film;
    }

    public Film addLike(Long filmId, Long userId) {
        log.info("FilmService: addLike(): start with id={}, userId={}", filmId, userId);
        Film film = filmRepository.getFilmById(filmId);

        film.getLikes().add(userId);

        likeRepository.addLikes(filmId, userId);

        return film;
    }

    public Film deleteLike(Long filmId, Long userId) {
        log.info("FilmService: deleteLike(): start with id={}, userId={}", filmId, userId);
        Film film = filmRepository.getFilmById(filmId);

        film.getLikes().remove(userId);

        likeRepository.deleteLike(filmId, userId);

        return film;
    }

    public List<Film> getMostPopularFilms(Integer count) {
        log.info("FilmService: getMostPopularFilms(): start with count={}", count);
        List<Film> mostPopularFilm = filmRepository.getMostPopularFilms(count);

        for (Film film : mostPopularFilm) {
            film.setGenres(genreRepository.getGenreByFilmId(film.getId()));
            film.setMpa(mpaRepository.getMpaByFilmId(film.getId()));
            film.setLikes(likeRepository.getLikesByFilmId(film.getId()));
        }

        return mostPopularFilm;
    }
}