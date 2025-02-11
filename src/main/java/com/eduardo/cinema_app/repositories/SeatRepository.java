package com.eduardo.cinema_app.repositories;

import com.eduardo.cinema_app.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
