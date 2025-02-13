package com.eduardo.cinema_app.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SessionResponseDTO(
        Long id,
        MovieResponseDTO movie,
        RoomResponseDTO room,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal pricePerSeat
) {
}
