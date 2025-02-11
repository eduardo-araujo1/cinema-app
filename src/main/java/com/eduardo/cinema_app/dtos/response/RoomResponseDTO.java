package com.eduardo.cinema_app.dtos.response;

public record RoomResponseDTO(
        Long id,
        Integer roomNumber,
        Integer totalSeats
) {
}
