package com.project.movieratingapp.controller;


import com.project.movieratingapp.model.Director;
import com.project.movieratingapp.service.DirectorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping()
    public List<Director> getDirectors() {
        return directorService.getDirectors();
    }

    @PostMapping()
    public Director addDirector(@Valid @RequestBody Director director) {
        return directorService.addDirector(director);
    }

    @PutMapping()
    public Director updateDirector(@Valid @RequestBody Director director) {
        return directorService.updateDirector(director);
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable Integer id) {
        return directorService.getDirectorById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDirectorById(@PathVariable Integer id) {
        directorService.deleteDirectorById(id);
    }
}
