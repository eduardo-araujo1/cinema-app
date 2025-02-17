package com.eduardo.cinema_app.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ticket_seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketSeat {

    @EmbeddedId
    private TicketSeatId id;

    @ManyToOne
    @MapsId("ticketId")
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @MapsId("seatId")
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

}
