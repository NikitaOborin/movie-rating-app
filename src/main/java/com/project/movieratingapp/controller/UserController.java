package com.project.movieratingapp.controller;

import com.project.movieratingapp.model.User;
import com.project.movieratingapp.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping()
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        return userRepository.addUser(user);
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) {
        return userRepository.updateUser(user);
    }
}