package com.example.book_review.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

public class DTOs {

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;
        private String fullName;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class LoginRequest {
        private String usernameOrEmail;
        private String password;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class AuthResponse {
        private String token;
        private UserDTO user;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class UserDTO {
        private Long id;
        private String username;
        private String email;
        private String fullName;
        private String avatarUrl;
        private String bio;
        private String role;
        private LocalDateTime createdAt;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class BookDTO {
        private Long id;
        private String title;
        private String author;
        private String description;
        private String coverUrl;
        private String genre;
        private String isbn;
        private Integer publishedYear;
        private String publisher;
        private Integer pageCount;
        private String language;
        private Double averageRating;
        private Long reviewCount;
        private LocalDateTime createdAt;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CreateBookRequest {
        private String title;
        private String author;
        private String description;
        private String coverUrl;
        private String genre;
        private String isbn;
        private Integer publishedYear;
        private String publisher;
        private Integer pageCount;
        private String language;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ReviewDTO {
        private Long id;
        private Long bookId;
        private String bookTitle;
        private String bookCoverUrl;
        private Long userId;
        private String username;
        private String userAvatarUrl;
        private Integer rating;
        private String title;
        private String content;
        private Integer likes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CreateReviewRequest {
        private Integer rating;
        private String title;
        private String content;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class PageResponse<T> {
        private java.util.List<T> content;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean last;
    }
}