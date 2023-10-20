package com.project.movieratingapp.service;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.Review;
import com.project.movieratingapp.model.User;
import com.project.movieratingapp.repository.event.EventRepository;
import com.project.movieratingapp.repository.film.FilmRepository;
import com.project.movieratingapp.repository.review.ReviewRepository;
import com.project.movieratingapp.repository.user.UserRepository;
import com.project.movieratingapp.util.EventType;
import com.project.movieratingapp.util.OperationType;
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
    private final EventRepository eventRepository;

    @Autowired
    public ReviewService(@Qualifier("userDBRepository") UserRepository userRepository,
                         @Qualifier("filmDBRepository") FilmRepository filmRepository,
                                                        ReviewRepository reviewRepository,
                                                        EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.reviewRepository = reviewRepository;
        this.eventRepository = eventRepository;
    }

    public Review addReview(Review review) {
        User user = userRepository.getUserById(review.getUserId());
        Film film = filmRepository.getFilmById(review.getFilmId());

        review = reviewRepository.addReview(review);

        eventRepository.addEventForUserByEntityId(user.getId(), review.getReviewId(), EventType.REVIEW.toString(), OperationType.ADD.toString());

        return review;
    }

    public Review updateReview(Review review) {
        review = reviewRepository.updateReview(review);

        eventRepository.addEventForUserByEntityId(review.getUserId(), review.getReviewId(), EventType.REVIEW.toString(), OperationType.UPDATE.toString());

        return review;
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepository.getReviewById(reviewId);
    }

    public void deleteReviewById(Long reviewId) {
        Review review = reviewRepository.getReviewById(reviewId);

        reviewRepository.deleteReviewById(reviewId);

        eventRepository.addEventForUserByEntityId(review.getUserId(), reviewId, EventType.REVIEW.toString(), OperationType.REMOVE.toString());
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
