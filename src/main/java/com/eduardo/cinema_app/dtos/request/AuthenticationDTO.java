package com.eduardo.cinema_app.dtos.request;

public record AuthenticationDTO(
        String email,
        String password
){}
