package com.project.movieratingapp.repository.review;

import com.project.movieratingapp.exception.NotFoundException;
import com.project.movieratingapp.model.Review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class ReviewDBRepository implements ReviewRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDBRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review addReview(Review review) {
        String sqlQuery = "INSERT INTO review (content, is_positive, user_id, film_id, useful) " +
                          "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, new String[]{"review_id"});
            preparedStatement.setString(1, review.getContent());
            preparedStatement.setBoolean(2, review.getIsPositive());
            preparedStatement.setLong(3, review.getUserId());
            preparedStatement.setLong(4, review.getFilmId());
            preparedStatement.setInt(5, review.getUseful());

            return preparedStatement;
        }, keyHolder);

        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return review;
    }

    @Override
    public Review updateReview(Review review) {
        jdbcTemplate.update("UPDATE review SET content=?, is_positive=? WHERE review_id=?",
                                review.getContent(), review.getIsPositive(), review.getReviewId());

        return getReviewById(review.getReviewId());
    }

    @Override
    public Review getReviewById(Long reviewId) {
        Review review;
        List<Review> reviews = jdbcTemplate.query("SELECT * FROM review WHERE review_id=?", reviewRowMapper, reviewId);

        if (reviews.isEmpty()) {
            throw new NotFoundException("review with id = " + reviewId + " not found");
        }

        review = reviews.get(0);

        return review;
    }

    @Override
    public void deleteReviewById(Long reviewId) {
        jdbcTemplate.update("DELETE FROM review WHERE review_id=?", reviewId);
    }

    @Override
    public List<Review> getReviews(Integer count) {
        return jdbcTemplate.query("SELECT * FROM review ORDER BY useful DESC LIMIT ?", reviewRowMapper, count);
    }

    @Override
    public List<Review> getReviewsByFilmId(Long filmId, Integer count) {
        return jdbcTemplate.query("SELECT * FROM review WHERE film_id=? ORDER BY useful DESC LIMIT ?",
                                  reviewRowMapper, filmId, count);
    }

    @Override
    public void addLikeForReview(Long reviewId, Long userId) {
        jdbcTemplate.update("UPDATE review SET useful = useful + 1 WHERE review_id=?", reviewId);
    }

    @Override
    public void addDislikeForReview(Long reviewId, Long userId) {
        jdbcTemplate.update("UPDATE review SET useful = useful - 1 WHERE review_id=?", reviewId);
    }

    private final RowMapper<Review> reviewRowMapper = (rs, rowNum) -> {
        Review review = new Review();

        review.setReviewId(rs.getLong("review_id"));
        review.setContent(rs.getString("content"));
        review.setIsPositive(rs.getBoolean("is_positive"));
        review.setUserId(rs.getLong("user_id"));
        review.setFilmId(rs.getLong("film_id"));
        review.setUseful(rs.getInt("useful"));

        return review;
    };
}
