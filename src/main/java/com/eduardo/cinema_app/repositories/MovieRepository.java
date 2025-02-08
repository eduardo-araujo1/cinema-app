package com.eduardo.cinema_app.repositories;

import com.eduardo.cinema_app.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
