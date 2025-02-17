package com.eduardo.cinema_app.repositories;

import com.eduardo.cinema_app.domain.TicketSeat;
import com.eduardo.cinema_app.domain.TicketSeatId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketSeatRepository extends JpaRepository<TicketSeat, TicketSeatId> {

}
