package com.project.movieratingapp.repository;

import com.project.movieratingapp.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class UserRepository {
    HashMap<Long, User> users = new HashMap<>();
    private long generatorId;

    private long generateId() {
        return ++generatorId;
    }

    public void createUser(User user) {
        users.put(generateId(), user);
    }

    public void updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        }
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
