package com.project.movieratingapp.repository.user;

import com.project.movieratingapp.model.User;

import java.util.List;

public interface UserRepository {
    User addUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    User getUserById(Long id);
}
