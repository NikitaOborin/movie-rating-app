package com.project.movieratingapp.controller;

import com.project.movieratingapp.model.User;
import com.project.movieratingapp.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping()
    public List<User> getUsers() {
        log.info("getUsers controller: start");
        return userRepository.getUsers();
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        log.info("addUser controller: start with {}", user);
        return userRepository.addUser(user);
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) {
        log.info("addUser controller: start with {}", user);
        return userRepository.updateUser(user);
    }
}