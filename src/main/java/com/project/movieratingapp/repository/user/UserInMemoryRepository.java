package com.project.movieratingapp.repository.user;

import com.project.movieratingapp.exception.NotFoundException;
import com.project.movieratingapp.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class UserInMemoryRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long generatorId;

    private long generateId() {
        return ++generatorId;
    }

    @Override
    public List<User> getUsers() {
        log.info("UserInMemoryRepository: getUsers(): start");
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        log.info("UserInMemoryRepository: addUser(): start with user={}", user);
        user.setId(generateId());
        if (user.getName() == null || user.getName().isEmpty() || user.getName().equals(" ")) {
            user.setName(user.getLogin());
            log.info("UserInMemoryRepository: addUser(): name=null, login assign for name");
        }
        users.put(generatorId, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("UserInMemoryRepository: updateUser(): start with user={}", user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new NotFoundException(user + " not found");
        }
    }

    @Override
    public User getUserById(Long id) {
        log.info("UserInMemoryRepository: getUserById(): start with id={}", id);
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("user with id = " + id + " not found");
        }
    }

    @Override
    public void deleteUserById(Long userId) {

    }
}
