package com.eduardo.cinema_app.dtos.response;

import com.eduardo.cinema_app.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record TicketResponseDTO(
        String id,
        List<String> seatNumbers,
        Status status,
        BigDecimal price,
        LocalDateTime createdAt
) {
}
