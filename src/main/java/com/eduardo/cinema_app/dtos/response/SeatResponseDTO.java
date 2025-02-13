package com.eduardo.cinema_app.dtos.response;

public record SeatResponseDTO (
         Long id,
         String seatNumber,
         Boolean available
){
}
