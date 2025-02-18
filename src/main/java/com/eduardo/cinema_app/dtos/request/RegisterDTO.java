package com.eduardo.cinema_app.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank(message = "O campo nome não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O Nome deve ter entre 3 até 100 caracteres")
        String name,

        @NotBlank(message = "O campo Email não pode ser vazio.")
        @Email(message = "Formato de email inválido.")
        String email,

        @NotBlank(message = "O campo senha não pode ser vazio.")
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial"
        )
        String password
) {
}
