package com.eduardo.cinema_app.dtos.response;

public record PaymentResponseDTO(
        String payment_url,
        TicketResponseDTO ticket
) {
}
