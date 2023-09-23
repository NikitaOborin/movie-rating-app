package com.project.movieratingapp.service;

import com.project.movieratingapp.model.User;
import com.project.movieratingapp.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        log.info("getUsers userService: start");
        return userRepository.getUsers();
    }

    public User addUser(User user) {
        log.info("addUser userService: start with {}", user);
        return userRepository.addUser(user);
    }

    public User updateUser(User user) {
        log.info("updateUser userService: start with {}", user);
        return userRepository.updateUser(user);
    }

    public User getUserById(Long userId) {
        log.info("getUserById userService: start with {}", userId);
        return userRepository.getUserById(userId);
    }

    public User addFriend(Long userId, Long friendId) {
        log.info("addFriend userService: start with {}, {}", userId, friendId);
        User user = userRepository.getUserById(userId);
        if (user.getFriends() != null) {
            user.getFriends().add(friendId);
        } else {
            log.info("addFriend userService: user friends list equals null");
            Set<Long> friendsId = new HashSet<>();
            friendsId.add(friendId);
            user.setFriends(friendsId);
        }

        User friend = userRepository.getUserById(friendId);
        if (friend.getFriends() != null) {
            friend.getFriends().add(user.getId());
        } else {
            log.info("addFriend userService: newFriend friends list equals null");
            Set<Long> friendsId = new HashSet<>();
            friendsId.add(user.getId());
            friend.setFriends(friendsId);
        }
        return user;
    }

    public User deleteFriend(Long userId, Long friendId) {
        log.info("deleteFriend userService: start with {}, {}", userId, friendId);
        userRepository.getUserById(userId).getFriends().remove(friendId);
        userRepository.getUserById(friendId).getFriends().remove(userId);
        return userRepository.getUserById(userId);
    }

    public List<User> getUserFriends(Long userId) {
        User user = userRepository.getUserById(userId);
        List<Long> userFriendsId = user.getFriends().stream().toList();
        List<User> userFriends = new ArrayList<>();

        for (Long id : userFriendsId) {
            userFriends.add(userRepository.getUserById(id));
        }
        return userFriends;
    }

    public List<User> getMutualFriends(Long userId, Long otherUserId) {
        log.info("getMutualFriends userService: start with {}, {}", userId, otherUserId);
        User user = userRepository.getUserById(userId);
        User otherUser = userRepository.getUserById(otherUserId);
        Set<Long> userFriendsId = user.getFriends();
        Set<Long> otherUserFriendsId = otherUser.getFriends();
        List<User> mutualFriends = new ArrayList<>();

        for (Long friendId : userFriendsId) {
            for (Long otherFriendId : otherUserFriendsId) {
                if (friendId.equals(otherFriendId)) {
                    mutualFriends.add(userRepository.getUserById(otherFriendId));
                }
            }
        }
        return mutualFriends;
    }
}