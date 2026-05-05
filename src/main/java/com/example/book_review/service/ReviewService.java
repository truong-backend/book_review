package com.example.book_review.service;


import com.example.book_review.dto.DTOs.*;
import com.example.book_review.entity.Review;
import com.example.book_review.entity.Book;
import com.example.book_review.entity.User;
import com.example.book_review.repository.BookRepository;
import com.example.book_review.repository.ReviewRepository;
import com.example.book_review.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public PageResponse<ReviewDTO> getBookReviews(Long bookId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Review> reviews = reviewRepository.findByBookId(bookId, pageable);
        return toPageResponse(reviews);
    }

    public PageResponse<ReviewDTO> getUserReviews(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Review> reviews = reviewRepository.findByUserId(userId, pageable);
        return toPageResponse(reviews);
    }

    public ReviewDTO createReview(Long bookId, String username, CreateReviewRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (reviewRepository.existsByBookIdAndUserId(bookId, user.getId()))
            throw new RuntimeException("You have already reviewed this book");

        Review review = Review.builder()
                .book(book)
                .user(user)
                .rating(request.getRating())
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        return toReviewDTO(reviewRepository.save(review));
    }

    public ReviewDTO updateReview(Long reviewId, String username, CreateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getUsername().equals(username))
            throw new RuntimeException("Not authorized");

        review.setRating(request.getRating());
        review.setTitle(request.getTitle());
        review.setContent(request.getContent());
        review.setUpdatedAt(LocalDateTime.now());

        return toReviewDTO(reviewRepository.save(review));
    }

    public void deleteReview(Long reviewId, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getUsername().equals(username))
            throw new RuntimeException("Not authorized");

        reviewRepository.delete(review);
    }

    public ReviewDTO likeReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setLikes(review.getLikes() + 1);
        return toReviewDTO(reviewRepository.save(review));
    }

    public ReviewDTO toReviewDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .bookId(review.getBook().getId())
                .bookTitle(review.getBook().getTitle())
                .bookCoverUrl(review.getBook().getCoverUrl())
                .userId(review.getUser().getId())
                .username(review.getUser().getUsername())
                .userAvatarUrl(review.getUser().getAvatarUrl())
                .rating(review.getRating())
                .title(review.getTitle())
                .content(review.getContent())
                .likes(review.getLikes())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    private PageResponse<ReviewDTO> toPageResponse(Page<Review> page) {
        return PageResponse.<ReviewDTO>builder()
                .content(page.getContent().stream().map(this::toReviewDTO).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}