package com.project.movieratingapp.repository.like;

import com.project.movieratingapp.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class LikeDBRepository implements LikeRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDBRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLikes(Long filmId, Long userId) {
        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, new String[]{"like_id"});
            preparedStatement.setLong(1, filmId);
            preparedStatement.setLong(2, userId);

            return preparedStatement;
        }, keyHolder);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        int countRow = jdbcTemplate.update("DELETE FROM likes WHERE film_id=? AND user_id=?", filmId, userId);

        if (countRow != 1) {
            throw new NotFoundException("user's like with userId = " + userId + " for film with filmId = " + filmId + " found");
        }
    }

    @Override
    public Set<Long> getLikesByFilmId(Long filmId) {
        List<Long> listUserId = jdbcTemplate.query("SELECT user_id FROM likes WHERE film_id=?", userLikeRowMapper, filmId);

        return new HashSet<>(listUserId);
    }

    @Override
    public Set<Long> getLikesByUserId(Long userId) {
        List<Long> listFilmId = jdbcTemplate.query("SELECT film_id FROM likes WHERE user_id=?", filmLikeRowMapper, userId);

        return new HashSet<>(listFilmId);
    }

    private final RowMapper<Long> userLikeRowMapper = (rs, rowNum) -> {
        return rs.getLong("user_id");
    };

    private final RowMapper<Long> filmLikeRowMapper = (rs, rowNum) -> {
        return rs.getLong("film_id");
    };
}
