package com.project.movieratingapp.repository.like;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.User;
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
    public Film addLikes(Film film, User user) {
        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, new String[]{"like_id"});
            preparedStatement.setLong(1, film.getId());
            preparedStatement.setLong(2, user.getId());
            return preparedStatement;
        }, keyHolder);

        return film;
    }

    @Override
    public Film deleteLike(Film film, User user) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id=? AND user_id=?", film.getId(), user.getId());

        return film;
    }

    @Override
    public Set<Long> getLikesForFilm(Film film) {
        List<Long> listUserId = jdbcTemplate.query("SELECT user_id FROM likes WHERE film_id=?", userLikeRowMapper, film.getId());
        return new HashSet<>(listUserId);
    }

    @Override
    public Set<Long> getLikesForUser(User user) {
        List<Long> listFilmId = jdbcTemplate.query("SELECT film_id FROM likes WHERE user_id=?", filmLikeRowMapper, user.getId());
        return new HashSet<>(listFilmId);
    }

    private final RowMapper<Long> userLikeRowMapper = (rs, rowNum) -> {
        return rs.getLong("user_id");
    };

    private final RowMapper<Long> filmLikeRowMapper = (rs, rowNum) -> {
        return rs.getLong("film_id");
    };
}
