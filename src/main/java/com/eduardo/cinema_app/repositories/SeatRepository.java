package com.eduardo.cinema_app.repositories;

import com.eduardo.cinema_app.domain.Seat;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByRoomId(Long roomId);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s " +
            "WHERE s.id IN :seatIds " +
            "AND NOT EXISTS (SELECT ts FROM TicketSeat ts WHERE ts.seat.id = s.id AND ts.session.id = :sessionId)")
    List<Seat> findAvailableSeatsWithLock(@Param("seatIds") List<Long> seatIds, @Param("sessionId") Long sessionId);
}
