package com.eduardo.cinema_app.repositories;

import com.eduardo.cinema_app.domain.Movie;
import com.eduardo.cinema_app.enums.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m WHERE " +
            "(:title IS NULL OR m.title LIKE %:title%) AND " +
            "(:genre IS NULL OR m.genre = :genre)")
    Page<Movie> findAllWithFilters(String title, Genre genre, Pageable pageable);

    Optional<Movie> findByTitleIgnoreCase(String title);
}