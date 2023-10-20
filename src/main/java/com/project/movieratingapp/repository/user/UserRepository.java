package com.project.movieratingapp.repository.user;

import com.project.movieratingapp.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getUsers();

    User addUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    void deleteUserById(Long userId);
}
