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
        log.info("getFilms filmService: start");
        return filmRepository.getFilms();
    }

    public Film addFilm(Film film) {
        log.info("addFilm filmService: start with {}", film);
        return filmRepository.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("updateFilm filmService: start with {}", film);
        return filmRepository.updateFilm(film);
    }

    public Film getFilmById(Long id) {
        return filmRepository.getFilmById(id);
    }

    public Film addLike(Long filmId, Long userId) {
        log.info("addLike filmService: start with {}, {}", filmId, userId);
        Film film = filmRepository.getFilmById(filmId);
        User user = userRepository.getUserById(userId);
        if (film.getLikes() != null) {
            film.getLikes().add(user.getId());
        } else {
            log.info("addLike filmService: film likes list equals null");
            Set<Long> usersId = new HashSet<>();
            usersId.add(user.getId());
            film.setLikes(usersId);
        }

        if (user.getFilmLikes() != null) {
            user.getFilmLikes().add(film.getId());
        } else {
            log.info("addLike filmService: user likes list equals null");
            Set<Long> filmsId = new HashSet<>();
            filmsId.add(film.getId());
            user.setFilmLikes(filmsId);
        }
        return film;
    }

    public Film deleteLike(Long filmId, Long id) {
        log.info("deleteLike filmService: start with {}, {}", filmId, id);
        Film film = filmRepository.getFilmById(filmId);
        User user = userRepository.getUserById(id);
        if (film.getLikes() != null) {
            film.getLikes().remove(id);
        }

        user.getFilmLikes().remove(film.getId());
        return film;
    }

    public List<Film> getMostPopularFilms(Integer count) {
        log.info("deleteLike getMostPopularFilms: start ");
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