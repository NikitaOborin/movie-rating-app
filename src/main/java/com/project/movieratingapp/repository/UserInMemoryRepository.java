package com.project.movieratingapp.repository;

import com.project.movieratingapp.model.User;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserInMemoryRepository implements UserRepository {
    HashMap<Long, User> users = new HashMap<>();
    private long generatorId;

    private long generateId() {
        return ++generatorId;
    }

    @Override
    public User addUser(User user) {
        user.setId(generateId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(generatorId, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
