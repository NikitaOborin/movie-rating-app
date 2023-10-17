package com.project.movieratingapp.repository.review;

import com.project.movieratingapp.model.Review;

import java.util.List;

public interface ReviewRepository {
    Review addReview(Review review);

    Review updateReview(Review review);

    Review getReviewById(Long reviewId);

    void deleteReviewById(Long reviewId);

    List<Review> getReviews(Integer count);

    List<Review> getReviewsByFilmId(Long filmId, Integer count);

    void addLikeForReview(Long reviewId, Long userId);

    void addDislikeForReview(Long reviewId, Long userId);
}
