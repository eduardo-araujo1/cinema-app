package com.eduardo.cinema_app.dtos.request;

import com.eduardo.cinema_app.enums.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record MovieRequestDTO(
        @NotBlank(message = "O título é obrigatório")
        @Size(min = 1, max = 100, message = "O título deve ter entre 1 e 100 caracteres")
        String title,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String description,

        @NotNull(message = "O gênero é obrigatório")
        Genre genre,

        MultipartFile image
) {
}
