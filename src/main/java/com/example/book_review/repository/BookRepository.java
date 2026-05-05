package com.example.book_review.repository;

import com.example.book_review.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByGenre(String genre, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<Book> search(@Param("q") String query, Pageable pageable);

    @Query("SELECT DISTINCT b.genre FROM Book b WHERE b.genre IS NOT NULL")
    List<String> findAllGenres();

    @Query("SELECT b FROM Book b LEFT JOIN b.reviews r GROUP BY b ORDER BY AVG(r.rating) DESC")
    Page<Book> findTopRated(Pageable pageable);

    @Query("SELECT b FROM Book b ORDER BY b.createdAt DESC")
    Page<Book> findNewest(Pageable pageable);
}