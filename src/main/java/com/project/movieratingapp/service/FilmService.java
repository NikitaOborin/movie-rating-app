package com.project.movieratingapp.service;

import com.project.movieratingapp.exception.NotFoundException;
import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.repository.director.DirectorRepository;
import com.project.movieratingapp.repository.event.EventRepository;
import com.project.movieratingapp.repository.film.FilmRepository;
import com.project.movieratingapp.repository.genre.GenreRepository;
import com.project.movieratingapp.repository.like.LikeRepository;
import com.project.movieratingapp.repository.mpa.MpaRepository;
import com.project.movieratingapp.util.EventType;
import com.project.movieratingapp.util.OperationType;
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
    private final DirectorRepository directorRepository;
    private final EventRepository eventRepository;

    @Autowired
    public FilmService(@Qualifier("filmDBRepository") FilmRepository filmRepository,
                       GenreRepository genreRepository,
                       MpaRepository mpaRepository,
                       LikeRepository likeRepository, DirectorRepository directorRepository, EventRepository eventRepository) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
        this.mpaRepository = mpaRepository;
        this.likeRepository = likeRepository;
        this.directorRepository = directorRepository;
        this.eventRepository = eventRepository;
    }

    public List<Film> getFilms() {
        log.info("FilmService: getFilms(): start");
        List<Film> films = filmRepository.getFilms();

        for (Film film : films) {
            film.setGenres(genreRepository.getGenreByFilmId(film.getId()));
            film.setMpa(mpaRepository.getMpaByFilmId(film.getId()));
            film.setLikes(likeRepository.getLikesByFilmId(film.getId()));
            film.setDirectors(directorRepository.getDirectorsByFilmId(film.getId()));
        }

        return films;
    }

    public Film addFilm(Film film) {
        log.info("FilmService: addFilm(): start with film={}", film);
        Film addedFilm =  filmRepository.addFilm(film);

        genreRepository.updateGenreInDbByFilm(film);
        directorRepository.updateDirectorInDbByFilm(film);

        return addedFilm;
    }

    public Film updateFilm(Film film) {
        log.info("FilmService: updateFilm(): start with film={}", film);
        filmRepository.updateFilm(film);

        genreRepository.updateGenreInDbByFilm(film);
        directorRepository.updateDirectorInDbByFilm(film);

        film.setGenres(genreRepository.getGenreByFilmId(film.getId()));
        film.setDirectors(directorRepository.getDirectorsByFilmId(film.getId()));

        return film;
    }

    public Film getFilmById(Long id) {
        log.info("FilmService: getFilmById(): start with id={}", id);
        Film film = filmRepository.getFilmById(id);

        film.setGenres(genreRepository.getGenreByFilmId(film.getId()));
        film.setMpa(mpaRepository.getMpaByFilmId(film.getId()));
        film.setLikes(likeRepository.getLikesByFilmId(film.getId()));
        film.setDirectors(directorRepository.getDirectorsByFilmId(film.getId()));

        return film;
    }

    public Film addLike(Long filmId, Long userId) {
        log.info("FilmService: addLike(): start with id={}, userId={}", filmId, userId);
        Film film = filmRepository.getFilmById(filmId);

        film.getLikes().add(userId);

        likeRepository.addLikes(filmId, userId);

        eventRepository.addEventForUserByEntityId(userId, filmId, EventType.LIKE.toString(), OperationType.ADD.toString());

        return film;
    }

    public Film deleteLike(Long filmId, Long userId) {
        log.info("FilmService: deleteLike(): start with id={}, userId={}", filmId, userId);
        Film film = filmRepository.getFilmById(filmId);

        film.getLikes().remove(userId);

        likeRepository.deleteLike(filmId, userId);

        eventRepository.addEventForUserByEntityId(userId, filmId, EventType.LIKE.toString(), OperationType.REMOVE.toString());

        return film;
    }

    public List<Film> getMostPopularFilms(Integer count) {
        log.info("FilmService: getMostPopularFilms(): start with count={}", count);
        List<Film> mostPopularFilm = filmRepository.getMostPopularFilms(count);

        for (Film film : mostPopularFilm) {
            film.setGenres(genreRepository.getGenreByFilmId(film.getId()));
            film.setMpa(mpaRepository.getMpaByFilmId(film.getId()));
            film.setLikes(likeRepository.getLikesByFilmId(film.getId()));
            film.setDirectors(directorRepository.getDirectorsByFilmId(film.getId()));
        }

        return mostPopularFilm;
    }

    public List<Film> getFilmsByDirectorId(Integer directorId, String sortBy) {
        List<Film> films = new ArrayList<>();

        if (sortBy.equals("year")) {
            films = filmRepository.getFilmsByDirectorIdSortByYear(directorId);
        } else if (sortBy.equals("likes")) {
            films = filmRepository.getFilmsByDirectorIdSortByLikes(directorId);
        }

        if (films.isEmpty()) {
            throw new NotFoundException("films with directorId = " + directorId + " not found");
        }

        for (Film film : films) {
            film.setGenres(genreRepository.getGenreByFilmId(film.getId()));
            film.setMpa(mpaRepository.getMpaByFilmId(film.getId()));
            film.setLikes(likeRepository.getLikesByFilmId(film.getId()));
            film.setDirectors(directorRepository.getDirectorsByFilmId(film.getId()));
        }

        return films;
    }

    public List<Film> getFilmsWithSubstring(String query, String by) {
        List<Film> films = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase();

        if (query.isEmpty() && by.isEmpty()) {
            films = getMostPopularFilms(Integer.MAX_VALUE);
        } else if (by.equals("director")) {
            films = filmRepository.getFilmsWithSubstringInDirector(lowerCaseQuery);
        } else if (by.equals("title")) {
            films = filmRepository.getFilmsWithSubstringInTitle(lowerCaseQuery);
        } else if (by.equals("director,title") || by.equals("title,director")) {
            films = filmRepository.getFilmsWithSubstringInDirectorAndTitle(lowerCaseQuery);
        }

        for (Film film : films) {
            film.setGenres(genreRepository.getGenreByFilmId(film.getId()));
            film.setMpa(mpaRepository.getMpaByFilmId(film.getId()));
            film.setLikes(likeRepository.getLikesByFilmId(film.getId()));
            film.setDirectors(directorRepository.getDirectorsByFilmId(film.getId()));
        }

        return films;
    }

    public List<Film> getCommonFilmsWithFriend(Long userId, Long friendId) {
        List<Film> commonFilms = new ArrayList<>();

        commonFilms =  filmRepository.getCommonFilmsWithFriend(userId, friendId);

        for (Film film : commonFilms) {
            film.setGenres(genreRepository.getGenreByFilmId(film.getId()));
            film.setMpa(mpaRepository.getMpaByFilmId(film.getId()));
            film.setLikes(likeRepository.getLikesByFilmId(film.getId()));
            film.setDirectors(directorRepository.getDirectorsByFilmId(film.getId()));
        }

        return commonFilms;
    }
}