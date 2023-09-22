package com.project.movieratingapp.service;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.User;
import com.project.movieratingapp.repository.FilmRepository;
import com.project.movieratingapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmServiceTest {
    private Film film1;
    private Film film2;
    private Film film3;
    private User user1;
    private final FilmService filmService;
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    @Autowired
    FilmServiceTest(FilmService filmService, FilmRepository filmRepository, UserRepository userRepository) {
        this.filmService = filmService;
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void createFilms() {
        film1 = new Film();
        film1.setId(1L);
        film1.setName("film1Name");
        film1.setDescription("film1Description");
        film1.setDuration(100);
        film1.setReleaseDate(LocalDate.of(2001, 1, 1));

        film2 = new Film();
        film2.setId(2L);
        film2.setName("film2Name");
        film2.setDescription("film2Description");
        film2.setDuration(200);
        film2.setReleaseDate(LocalDate.of(2002, 2, 2));

        film3 = new Film();
        film3.setId(3L);
        film3.setName("film2Name");
        film3.setDescription("film2Description");
        film3.setDuration(300);
        film3.setReleaseDate(LocalDate.of(2003, 3, 3));

        user1 = new User();
        user1.setId(1L);
        user1.setName("user1Name");
        user1.setBirthday(LocalDate.of(2001, 1, 1));
        user1.setLogin("user1Login");
        user1.setEmail("user1@mail.ru");

        filmRepository.addFilm(film1);
        filmRepository.addFilm(film2);
        filmRepository.addFilm(film3);
        userRepository.addUser(user1);
    }

    @Test
    void shouldSuccessAddLike() {
        filmService.addLike(film1.getId(), user1.getId());
        assertEquals(film1.getLikes().size(), 1, "У фильма нет лайков");
        assertEquals(user1.getFilmLikes().size(), 1, "У пользователя нет лайков");
    }

    @Test
    void shouldSuccessDeleteLike() {
        filmService.addLike(film1.getId(), user1.getId());
        filmService.deleteLike(film1.getId(), 1L);
        assertEquals(film1.getLikes().size(), 0, "У фильма есть лайка");
        assertEquals(user1.getFilmLikes().size(), 0, "У пользователя есть лайки");
    }

//    @Test
//    void shouldSuccessGetMostPopularFilms() {
//        Integer count = 10;
//
//        Set<Long> set1 = new HashSet<>();
//        set1.add(1L);
//        film1.setLikes(set1);
//
//        Set<Long> set2 = new HashSet<>();
//        set2.add(1L);
//        set2.add(2L);
//        film2.setLikes(set2);
//
//        Set<Long> set3 = new HashSet<>();
//        set3.add(1L);
//        set3.add(2L);
//        set3.add(3L);
//        film3.setLikes(set3);
//
//        filmRepository.updateFilm(film1);
//        filmRepository.updateFilm(film2);
//        filmRepository.updateFilm(film3);
//
//        List<Film> expectFilmsList = new ArrayList<>();
//        expectFilmsList.add(film3);
//        expectFilmsList.add(film2);
//        expectFilmsList.add(film1);
//
//        assertEquals(expectFilmsList, filmService.getMostPopularFilms(count), "Фильмы неверно отсортированы");
//    }
}