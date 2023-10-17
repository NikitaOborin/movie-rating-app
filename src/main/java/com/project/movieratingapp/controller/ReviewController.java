package com.project.movieratingapp.controller;

import com.project.movieratingapp.model.Review;
import com.project.movieratingapp.service.ReviewService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping()
    public Review addReview(@Valid @RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @PutMapping()
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteReviewById(@PathVariable Long id) {
        reviewService.deleteReviewById(id);
    }

    @GetMapping()
    public List<Review> getReviews(@RequestParam(required = false) Long filmId,
                                   @RequestParam(defaultValue = "10") Integer count) {
        return reviewService.getReviews(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeForReviewByUserId(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.addLikeForReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislikeForReviewByUserId(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.addDislikeForReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeForReviewByUserId(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.deleteLikeForReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislikeForReviewByUserId(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.deleteDislikeForReview(id, userId);
    }
}
