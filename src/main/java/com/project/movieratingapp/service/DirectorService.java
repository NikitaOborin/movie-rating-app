package com.project.movieratingapp.service;

import com.project.movieratingapp.model.Director;
import com.project.movieratingapp.repository.director.DirectorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DirectorService {
    private final DirectorRepository directorRepository;

    @Autowired
    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public List<Director> getDirectors() {
        return directorRepository.getDirectors();
    }

    public Director addDirector(Director director) {
        return directorRepository.addDirector(director);
    }

    public Director updateDirector(Director director) {
        return directorRepository.updateDirector(director);
    }

    public Director getDirectorById(Integer directorId) {
        return directorRepository.getDirectorById(directorId);
    }

    public void deleteDirectorById(Integer directorId) {
        directorRepository.deleteDirectorById(directorId);
    }
}
