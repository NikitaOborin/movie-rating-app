package com.project.movieratingapp.repository.film;

import com.project.movieratingapp.exception.NotFoundException;
import com.project.movieratingapp.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class FilmInMemoryRepository implements FilmRepository {
    private final Map<Long, Film> films = new HashMap<>();
    private long generatorId;

    private long generateId() {
        return ++generatorId;
    }

    @Override
    public List<Film> getFilms() {
        log.info("FilmInMemoryRepository: getFilms(): start");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        log.info("FilmInMemoryRepository: addFilm(): start with film={}", film);
        film.setId(generateId());
        films.put(generatorId, film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("FilmInMemoryRepository: updateFilm(): start with film={}", film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new NotFoundException(film + " not found");
        }
    }

    @Override
    public Film getFilmById(Long id) {
        log.info("FilmInMemoryRepository: getFilmById(): start with id={}", id);
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException("film with id = " + id + " not found");
        }
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        List<Film> filmList = (List<Film>) films.values();
        List<Film> mostPopularFilms = new ArrayList<>();

        filmList.sort(new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return o2.getLikes().size() - o1.getLikes().size();
            }
        });

        for (Film film : filmList) {
            if (mostPopularFilms.size() < count) {
                mostPopularFilms.add(film);
            }
        }

        return mostPopularFilms;
    }

    @Override
    public List<Film> getFilmsByDirectorIdSortByYear(Integer directorId) {
        return null;
    }

    @Override
    public List<Film> getFilmsByDirectorIdSortByLikes(Integer directorId) {
        return null;
    }

    @Override
    public List<Film> getFilmsWithSubstringInDirector(String query) {
        return null;
    }

    @Override
    public List<Film> getFilmsWithSubstringInTitle(String query) {
        return null;
    }

    @Override
    public List<Film> getFilmsWithSubstringInDirectorAndTitle(String query) {
        return null;
    }

    @Override
    public List<Film> getCommonFilmsWithFriend(Long userId, Long friendId) {
        return null;
    }

    @Override
    public List<Film> getFilmsRecommendation(Long userId) {
        return null;
    }
}