package com.eduardo.cinema_app.repositories;

import com.eduardo.cinema_app.domain.Ticket;
import com.eduardo.cinema_app.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, String> {

    List<Ticket> findByStatusAndCreatedAtLessThan(Status status, LocalDateTime dateTime);
}
