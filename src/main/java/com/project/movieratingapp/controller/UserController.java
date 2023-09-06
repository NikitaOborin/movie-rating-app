package com.project.movieratingapp.controller;

import com.project.movieratingapp.model.User;
import com.project.movieratingapp.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    UserRepository userRepository;

    @GetMapping()
    public List<User> getUsers() {
        return userRepository.getUsers();
    }
}
