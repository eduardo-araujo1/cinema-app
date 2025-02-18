package com.eduardo.cinema_app.dtos.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SessionRequestDTO(

        @NotBlank(message = "O título do filme não pode ser vazio.")
        String movieTitle,

        @NotNull(message = "O número da sala não pode ser nulo.")
        @Min(value = 1, message = "O número da sala deve ser pelo menos 1.")
        Integer roomNumber,

        @NotNull(message = "A hora de início da sessão não pode ser nula.")
        @Future(message = "O horário de início da sessão deve ser no futuro.")
        LocalDateTime startTime,

        @NotNull(message = "O horário do final da sessão não pode ser nulo.")
        @Future(message = "O horário do final da sessão deve ser no futuro.")
        LocalDateTime endTime,

        @NotNull(message = "O preço por cadeira não pode ser nulo.")
        @DecimalMin(value = "5.00", message = "O preço por assento deve ser pelo menos R$5.00.")
        BigDecimal pricePerSeat
) {
}
