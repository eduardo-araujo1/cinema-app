package com.eduardo.cinema_app.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class TicketSeatId implements Serializable {
    private String ticketId;
    private Long seatId;

}
