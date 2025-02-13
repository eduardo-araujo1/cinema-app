package com.eduardo.cinema_app.dtos.response;

import java.time.LocalDateTime;

public record MovieSessionDTO(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
