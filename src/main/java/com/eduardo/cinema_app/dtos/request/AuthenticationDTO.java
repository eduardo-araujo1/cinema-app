package com.eduardo.cinema_app.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank(message = "O Campo email não pode ser vazio")
        @Email(message = "Formato de email inválido.")
        String email,

        @NotBlank(message = "O campo senha não pode ser vazio.")
        String password
){}
