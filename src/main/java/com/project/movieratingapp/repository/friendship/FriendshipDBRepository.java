package com.project.movieratingapp.repository.friendship;

import com.project.movieratingapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FriendshipDBRepository implements FriendshipRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDBRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User updateFriendshipInDBForUser(User user) {
        deleteAllFriendsForUser(user);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "INSERT INTO friendship (user_id, friend_id, status) VALUES (?, ?, ?)";
        Long userId = user.getId();

        for (Long friendId : user.getFriends().keySet()) {
            Boolean friendshipStatus = user.getFriends().get(friendId);

            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, new String[]{"friendship_id"});
                preparedStatement.setLong(1, userId);
                preparedStatement.setLong(2, friendId);
                preparedStatement.setBoolean(3, friendshipStatus);
                return preparedStatement;
            }, keyHolder);
        }

        return user;
    }

    @Override
    public Map<Long, Boolean> getFriendshipMapForUser(User user) {
        Map<Long, Boolean> friendshipMap = new HashMap<>();
        List<Map<Long, Boolean>> listFriendshipMap = jdbcTemplate.query("SELECT friend_id, status FROM friendship WHERE user_id=?",
                                                                       rowMapperFriendship, user.getId());

        for (Map<Long, Boolean> friendshipStatus : listFriendshipMap) {
            Long key;
            Boolean value;

            for (Long friendId : friendshipStatus.keySet()) {
                key = friendId;
                value = friendshipStatus.get(key);

                friendshipMap.put(key, value);
            }
        }

        return friendshipMap;
    }

    @Override
    public void deleteAllFriendsForUser(User user) {
        jdbcTemplate.update("DELETE FROM friendship WHERE user_id=?", user.getId());
    }

    private final RowMapper<Map<Long, Boolean>> rowMapperFriendship = (rs, rowNum) -> {
      Map<Long, Boolean> friendshipMap = new HashMap<>();
      Long key = rs.getLong("friend_id");
      Boolean value = rs.getBoolean("status");
      friendshipMap.put(key, value);

      return friendshipMap;
    };
}
