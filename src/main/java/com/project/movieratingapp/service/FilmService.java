package com.project.movieratingapp.service;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.User;
import com.project.movieratingapp.repository.FilmRepository;
import com.project.movieratingapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Film addLike(Film film, User user) {
        if (film.getLikes() != null) {
            film.getLikes().add(user.getId());
        } else {
            Set<Long> usersId = new HashSet<>();
            usersId.add(user.getId());
            film.setLikes(usersId);
        }

        if (user.getFilmLikes() != null) {
            user.getFilmLikes().add(film.getId());
        } else {
            Set<Long> filmsId = new HashSet<>();
            filmsId.add(film.getId());
            user.setFilmLikes(filmsId);
        }
        return film;
    }

    public Film deleteLike(Film film, Long id) {
        if (film.getLikes() != null) {
            film.getLikes().remove(id);
        }

        User user = userRepository.getUserById(id);
        user.getFilmLikes().remove(film.getId());
        return film;
    }

    public List<Film> getMostPopularFilms() {
        
        return null;
    }
}
