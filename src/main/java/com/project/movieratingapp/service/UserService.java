package com.project.movieratingapp.service;

import com.project.movieratingapp.model.User;
import com.project.movieratingapp.repository.friendship.FriendshipRepository;
import com.project.movieratingapp.repository.like.LikeRepository;
import com.project.movieratingapp.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public UserService(@Qualifier("userDBRepository") UserRepository userRepository,
                                                      FriendshipRepository friendshipRepository,
                                                      LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.likeRepository = likeRepository;
    }

    public List<User> getUsers() {
        log.info("UserService: getUsers(): start");
        List<User> users = userRepository.getUsers();

        for (User user : users) {
            user.setFriends(friendshipRepository.getFriendsByUserId(user.getId()));
            user.setFilmLikes(likeRepository.getLikesByUserId(user.getId()));
        }

        return users;
    }

    public User addUser(User user) {
        log.info("UserService: addUser(): start with user={}", user);
        return userRepository.addUser(user);
    }

    public User updateUser(User user) {
        log.info("UserService: updateUser(): start with user={}", user);
        return userRepository.updateUser(user);
    }

    public User getUserById(Long userId) {
        log.info("UserService: getUserById(): start with id={}", userId);
        User user = userRepository.getUserById(userId);

        user.setFriends(friendshipRepository.getFriendsByUserId(userId));
        user.setFilmLikes(likeRepository.getLikesByUserId(userId));

        return user;
    }

    public User addFriend(Long userId, Long friendId) {
        log.info("UserService: addFriend(): start with id={}, friendId={}", userId, friendId);
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (friend.getFriends().containsKey(userId)) {
            friend.getFriends().put(userId, true);
            user.getFriends().put(friendId, true);

            friendshipRepository.addFriendship(user, friendId);
            friendshipRepository.addFriendship(friend, userId);
        } else {
            user.getFriends().put(friendId, false);

            friendshipRepository.addFriendship(user, friendId);
        }

        return user;
    }

    public User deleteFriend(Long userId, Long friendId) {
        log.info("UserService: deleteFriend(): start with id={}, friendId={}", userId, friendId);
        User user = getUserById(userId);

        friendshipRepository.deleteFriendship(userId, friendId);

        if (user.getFriends().get(friendId).equals(true)) {
            friendshipRepository.deleteFriendship(friendId, userId);
        }

        user.setFriends(friendshipRepository.getFriendsByUserId(userId));

        return user;
    }

    public List<User> getUserFriends(Long userId) {
        log.info("UserService: getUserFriends(): start with id={}", userId);
        Map<Long, Boolean> getFriendsByUserId = friendshipRepository.getFriendsByUserId(userId);
        Set<Long> userFriendsId = getFriendsByUserId.keySet();

        List<User> userFriends = new ArrayList<>();

        for (Long friendId : userFriendsId) {
            userFriends.add(userRepository.getUserById(friendId));
        }

        return userFriends;
    }

    public List<User> getMutualFriends(Long userId, Long otherId) {
        log.info("UserService: getMutualFriends(): start with id={}, otherId={}", userId, otherId);
        List<User> mutualFriends = new ArrayList<>();
        User user = getUserById(userId);
        User otherUser = getUserById(otherId);
        Set<Long> userFriendsId = user.getFriends().keySet();
        Set<Long> otherUserFriendsId = otherUser.getFriends().keySet();

        for (Long friendId : userFriendsId) {
            for (Long otherFriendId : otherUserFriendsId) {
                if (friendId.equals(otherFriendId)) {
                    mutualFriends.add(getUserById(otherFriendId));
                }
            }
        }

        return mutualFriends;
    }
}