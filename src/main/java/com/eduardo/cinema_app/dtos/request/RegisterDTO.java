package com.eduardo.cinema_app.dtos.request;

public record RegisterDTO(
        String name,
        String email,
        String password
) {
}
