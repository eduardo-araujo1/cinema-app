package com.eduardo.cinema_app.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SessionRequestDTO(

        String movieTitle,
        Integer roomNumber,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal pricePerSeat
) {
}
