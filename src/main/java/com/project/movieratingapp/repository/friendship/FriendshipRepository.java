package com.project.movieratingapp.repository.friendship;

import com.project.movieratingapp.model.User;

import java.util.Map;

public interface FriendshipRepository {
    User updateFriendshipInDBForUser(User user);

    Map<Long, Boolean> getFriendshipMapForUser(User user);

    void deleteAllFriendsForUser (User user);
}
