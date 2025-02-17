package com.eduardo.cinema_app.dtos.request;

import java.util.List;

public record TicketRequestDTO(
        Long sessionId,
        Long customerId,
        List<Long> seatsId
) {
}