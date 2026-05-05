package com.example.book_review.controller;

import com.example.book_review.dto.DTOs.*;
import com.example.book_review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/books/{bookId}/reviews")
    public ResponseEntity<PageResponse<ReviewDTO>> getBookReviews(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.getBookReviews(bookId, page, size));
    }

    @GetMapping("/users/{userId}/reviews")
    public ResponseEntity<PageResponse<ReviewDTO>> getUserReviews(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.getUserReviews(userId, page, size));
    }

    @PostMapping("/books/{bookId}/reviews")
    public ResponseEntity<ReviewDTO> createReview(
            @PathVariable Long bookId,
            @RequestBody CreateReviewRequest request,
            Authentication auth) {
        return ResponseEntity.ok(reviewService.createReview(bookId, auth.getName(), request));
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long reviewId,
            @RequestBody CreateReviewRequest request,
            Authentication auth) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, auth.getName(), request));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId, Authentication auth) {
        reviewService.deleteReview(reviewId, auth.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reviews/{reviewId}/like")
    public ResponseEntity<ReviewDTO> likeReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.likeReview(reviewId));
    }
}