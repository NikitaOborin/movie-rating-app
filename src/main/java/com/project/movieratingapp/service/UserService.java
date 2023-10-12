package com.project.movieratingapp.service;

import com.project.movieratingapp.model.User;
import com.project.movieratingapp.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userDBRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        log.info("UserService: getUsers(): start");
        return userRepository.getUsers();
    }

    public User addUser(User user) {
        log.info("UserService: addUser(): start with user={}", user);
        return userRepository.addUser(user);
    }

    public User updateUser(User user) {
        log.info("UserService: updateUser(): start with user={}", user);
        return userRepository.updateUser(user);
    }

    public User getUserById(Long id) {
        log.info("UserService: getUserById(): start with id={}", id);
        return userRepository.getUserById(id);
    }

    public User addFriend(Long id, Long friendId) {
        log.info("UserService: addFriend(): start with id={}, friendId={}", id, friendId);
        User user = userRepository.getUserById(id);
        User friend = userRepository.getUserById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(user.getId());
        return user;
    }

    public User deleteFriend(Long id, Long friendId) {
        log.info("UserService: deleteFriend(): start with id={}, friendId={}", id, friendId);
        userRepository.getUserById(id).getFriends().remove(friendId);
        userRepository.getUserById(friendId).getFriends().remove(id);
        return userRepository.getUserById(id);
    }

    public List<User> getUserFriends(Long id) {
        log.info("UserService: getUserFriends(): start with id={}", id);
        User user = userRepository.getUserById(id);
        List<Long> userFriendsId = user.getFriends().stream().toList();
        List<User> userFriends = new ArrayList<>();

        for (Long friendId : userFriendsId) {
            userFriends.add(userRepository.getUserById(friendId));
        }
        return userFriends;
    }

    public List<User> getMutualFriends(Long id, Long otherId) {
        log.info("UserService: getMutualFriends(): start with id={}, otherId={}", id, otherId);
        User user = userRepository.getUserById(id);
        User otherUser = userRepository.getUserById(otherId);
        List<User> mutualFriends = new ArrayList<>();
        Set<Long> userFriendsId = user.getFriends();
        Set<Long> otherUserFriendsId = otherUser.getFriends();

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