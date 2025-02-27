package com.eduardo.cinema_app.dtos.response;

import com.eduardo.cinema_app.domain.Customer;

public record LoginResponseDTO(
        String token,
        Long id,
        String name,
        String email
) {

    public LoginResponseDTO(Customer customer, String token) {
        this(token, customer.getId(), customer.getName(), customer.getEmail());
    }
}
