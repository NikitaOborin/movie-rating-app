package com.project.movieratingapp.service;

import com.project.movieratingapp.model.User;
import com.project.movieratingapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Long addFriend(User user, User newFriend) {
        log.info("addFriend userService: start with {}, {}", user, newFriend);
        if (user.getFriends() != null) {
            log.info("addFriend userService: list friends for user equals null");
            user.getFriends().add(newFriend.getId());
        } else {
            Set<Long> friendsId = new HashSet<>();
            friendsId.add(newFriend.getId());
            user.setFriends(friendsId);
        }

        if (newFriend.getFriends() != null) {
            newFriend.getFriends().add(user.getId());
        } else {
            log.info("addFriend userService: list friends for newFriend equals null");
            Set<Long> friendsId = new HashSet<>();
            friendsId.add(user.getId());
            newFriend.setFriends(friendsId);
        }
        return newFriend.getId();
    }

    public void deleteFriend(User user, User friend) {
        log.info("deleteFriend userService: start with {}, {}", user, friend);
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    public List<User> getMutualFriends(User user, User otherUser) {
        log.info("getMutualFriends userService: start with {}, {}", user, otherUser);
        Set<Long> userFriendsId = user.getFriends();
        Set<Long> otherUserFriendsId = otherUser.getFriends();
        List<Long> mutualFriendsId = new ArrayList<>();

        for (Long friendId : userFriendsId) {
            for (Long otherFriendId : otherUserFriendsId) {
                if (friendId.equals(otherFriendId)) {
                    mutualFriendsId.add(friendId);
                }
            }
        }

        List<User> mutualFriends = new ArrayList<>();
        for (User someUser : userRepository.getUsers()) {
            for (Long id : mutualFriendsId) {
                if (someUser.getId().equals(id)) {
                    mutualFriends.add(someUser);
                }
            }
        }
        return mutualFriends;
    }
}