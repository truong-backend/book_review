package com.example.book_review.service;

import com.example.book_review.dto.DTOs.*;
import com.example.book_review.entity.Book;
import com.example.book_review.repository.BookRepository;
import com.example.book_review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

    public PageResponse<BookDTO> getAllBooks(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Book> books;

        if ("top-rated".equals(sort)) {
            books = bookRepository.findTopRated(pageable);
        } else {
            books = bookRepository.findNewest(pageable);
        }

        return toPageResponse(books);
    }

    public PageResponse<BookDTO> searchBooks(String query, String genre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books;

        if (query != null && !query.isBlank()) {
            books = bookRepository.search(query.trim(), pageable);
        } else if (genre != null && !genre.isBlank()) {
            books = bookRepository.findByGenre(genre, pageable);
        } else {
            books = bookRepository.findAll(pageable);
        }

        return toPageResponse(books);
    }

    public BookDTO getBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return toBookDTO(book);
    }

    public BookDTO createBook(CreateBookRequest request) {
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .description(request.getDescription())
                .coverUrl(request.getCoverUrl())
                .genre(request.getGenre())
                .isbn(request.getIsbn())
                .publishedYear(request.getPublishedYear())
                .publisher(request.getPublisher())
                .pageCount(request.getPageCount())
                .language(request.getLanguage())
                .build();
        return toBookDTO(bookRepository.save(book));
    }

    public BookDTO updateBook(Long id, CreateBookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setDescription(request.getDescription());
        book.setCoverUrl(request.getCoverUrl());
        book.setGenre(request.getGenre());
        book.setIsbn(request.getIsbn());
        book.setPublishedYear(request.getPublishedYear());
        book.setPublisher(request.getPublisher());
        book.setPageCount(request.getPageCount());
        book.setLanguage(request.getLanguage());
        return toBookDTO(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public List<String> getGenres() {
        return bookRepository.findAllGenres();
    }

    public BookDTO toBookDTO(Book book) {
        Double avgRating = reviewRepository.getAverageRatingByBookId(book.getId());
        Long reviewCount = reviewRepository.countByBookId(book.getId());

        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .description(book.getDescription())
                .coverUrl(book.getCoverUrl())
                .genre(book.getGenre())
                .isbn(book.getIsbn())
                .publishedYear(book.getPublishedYear())
                .publisher(book.getPublisher())
                .pageCount(book.getPageCount())
                .language(book.getLanguage())
                .averageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : null)
                .reviewCount(reviewCount)
                .createdAt(book.getCreatedAt())
                .build();
    }

    private PageResponse<BookDTO> toPageResponse(Page<Book> page) {
        return PageResponse.<BookDTO>builder()
                .content(page.getContent().stream().map(this::toBookDTO).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}