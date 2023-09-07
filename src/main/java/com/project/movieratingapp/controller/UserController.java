package com.project.movieratingapp.controller;

import com.project.movieratingapp.model.User;
import com.project.movieratingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public User addUser(@RequestBody User user) {
        return userRepository.addUser(user);
    }

    @PatchMapping()
    public User updateUser(@RequestBody User user) {
        return userRepository.updateUser(user);
    }
}