package com.project.movieratingapp.service;

import com.project.movieratingapp.model.Genre;
import com.project.movieratingapp.repository.genre.GenreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GenreService {
    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getListGenre() {
        return genreRepository.getListGenre();
    }

    public Genre getGenreById(Integer genreId) {
        return genreRepository.getGenreByGenreId(genreId);
    }
}
