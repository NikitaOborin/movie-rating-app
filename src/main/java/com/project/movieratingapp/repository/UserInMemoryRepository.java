package com.project.movieratingapp.repository;

import com.project.movieratingapp.exception.NotFoundException;
import com.project.movieratingapp.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Repository
public class UserInMemoryRepository implements UserRepository {
    private final HashMap<Long, User> users = new HashMap<>();
    private long generatorId;

    private long generateId() {
        return ++generatorId;
    }

    @Override
    public User addUser(User user) {
        log.info("addUser repository: start with {}", user);
        user.setId(generateId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.info("addUser repository: name=null, login assign for name");
        }
        users.put(generatorId, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("updateUser repository: start with {}", user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new NotFoundException(user + " not found");
        }
    }

    @Override
    public List<User> getUsers() {
        log.info("getUsers repository: start");
        return new ArrayList<>(users.values());
    }
}
