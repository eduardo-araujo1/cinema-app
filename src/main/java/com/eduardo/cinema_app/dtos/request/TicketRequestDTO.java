package com.eduardo.cinema_app.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TicketRequestDTO(
        @NotNull(message = "o ID da sessão não pode ser nulo.")
        Long sessionId,

        @NotNull(message = "o ID do customer não pode ser nulo.")
        Long customerId,

        @NotEmpty(message = "Pelo menos um assento deve ser selecionado.")
        List<Long> seatsId
) {
}