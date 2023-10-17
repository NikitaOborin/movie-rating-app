package com.project.movieratingapp.service;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.Review;
import com.project.movieratingapp.model.User;
import com.project.movieratingapp.repository.film.FilmRepository;
import com.project.movieratingapp.repository.review.ReviewRepository;
import com.project.movieratingapp.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         @Qualifier("userDBRepository") UserRepository userRepository,
                         @Qualifier("filmDBRepository") FilmRepository filmRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
    }

    public Review addReview(Review review) {
        User user = userRepository.getUserById(review.getUserId());
        Film film = filmRepository.getFilmById(review.getFilmId());

        return reviewRepository.addReview(review);
    }

    public Review updateReview(Review review) {
        return reviewRepository.updateReview(review);
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepository.getReviewById(reviewId);
    }

    public void deleteReviewById(Long reviewId) {
        reviewRepository.deleteReviewById(reviewId);
    }

    public List<Review> getReviews(Long filmId, Integer count) {
        List<Review> reviews;

        if (filmId == null) {
            reviews = reviewRepository.getReviews(count);
        } else {
            reviews = reviewRepository.getReviewsByFilmId(filmId, count);
        }

        return reviews;
    }

    public void addLikeForReview(Long reviewId, Long userId) {
        reviewRepository.addLikeForReview(reviewId, userId);
    }

    public void addDislikeForReview(Long reviewId, Long userId) {
        reviewRepository.addDislikeForReview(reviewId, userId);
    }

    public void deleteLikeForReview(Long reviewId, Long userId) {
        //
    }

    public void deleteDislikeForReview(Long reviewId, Long userId) {
        //
    }
 }
