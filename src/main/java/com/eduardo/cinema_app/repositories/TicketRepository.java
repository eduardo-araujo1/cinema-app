package com.eduardo.cinema_app.repositories;

import com.eduardo.cinema_app.domain.Ticket;
import com.eduardo.cinema_app.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, String> {

    List<Ticket> findByStatusAndCreatedAtLessThan(Status status, LocalDateTime dateTime);

    @Query("SELECT t FROM Ticket t " +
            "JOIN FETCH t.customer " +
            "JOIN FETCH t.session s " +
            "JOIN FETCH s.movie " +
            "JOIN FETCH s.room " +
            "JOIN FETCH t.ticketSeats ts " +
            "JOIN FETCH ts.seat " +
            "WHERE t.id = :ticketId")
    Optional<Ticket> findByIdWithDetails(@Param("ticketId") String ticketId);
}
