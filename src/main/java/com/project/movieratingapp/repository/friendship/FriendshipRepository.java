package com.project.movieratingapp.repository.friendship;

import com.project.movieratingapp.model.User;

import java.util.Map;

public interface FriendshipRepository {
    void addFriendship(User user, Long friendId);

    void deleteFriendship(Long userId, Long friendId);

    Map<Long, Boolean> getFriendsByUserId(Long userId);
}
