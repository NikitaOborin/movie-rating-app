package com.project.movieratingapp.repository;

import com.project.movieratingapp.model.User;

import java.util.List;

public interface UserRepository {
    public User addUser(User user);
    public User updateUser(User user);
    public List<User> getUsers();
}
