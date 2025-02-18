package com.eduardo.cinema_app.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomRequestDTO(

        @NotNull(message = "O número da sala não pode ser nulo.")
        @Min(value = 1, message = "O número da sala deve ser pelo menos 1.")
        Integer roomNumber,

        @NotNull(message = "O número total de cadeiras deve ser informado.")
        @Min(value = 15, message = "O total de assentos deve ser pelo menos 15.")
        Integer totalSeats
) {
}
