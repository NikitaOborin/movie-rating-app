package com.project.movieratingapp.service;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.User;
import com.project.movieratingapp.repository.FilmRepository;
import com.project.movieratingapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository, UserRepository userRepository) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
    }

    public List<Film> getFilms() {
        log.info("FilmService: getFilms(): start");
        return filmRepository.getFilms();
    }

    public Film addFilm(Film film) {
        log.info("FilmService: addFilm(): start with film={}", film);
        return filmRepository.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("FilmService: updateFilm(): start with film={}", film);
        return filmRepository.updateFilm(film);
    }

    public Film getFilmById(Long id) {
        log.info("FilmService: getFilmById(): start with id={}", id);
        return filmRepository.getFilmById(id);
    }

    public Film addLike(Long id, Long userId) {
        log.info("FilmService: addLike(): start with id={}, userId={}", id, userId);
        Film film = filmRepository.getFilmById(id);
        User user = userRepository.getUserById(userId);

        film.getLikes().add(user.getId());
        user.getFilmLikes().add(film.getId());
        return film;
    }

    public Film deleteLike(Long id, Long userId) {
        log.info("FilmService: deleteLike(): start with id={}, userId={}", id, userId);
        Film film = filmRepository.getFilmById(id);
        User user = userRepository.getUserById(userId);

        film.getLikes().remove(userId);
        user.getFilmLikes().remove(film.getId());
        return film;
    }

    public List<Film> getMostPopularFilms(Integer count) {
        log.info("FilmService: getMostPopularFilms(): start with count={}", count);
        List<Film> films = filmRepository.getFilms();
        List<Film> mostPopularFilms = new ArrayList<>();

        films.sort(new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return o2.getLikes().size() - o1.getLikes().size();
            }
        });

        for (Film film : films) {
           if (mostPopularFilms.size() < count) {
               mostPopularFilms.add(film);
           }
        }
        return mostPopularFilms;
    }
}