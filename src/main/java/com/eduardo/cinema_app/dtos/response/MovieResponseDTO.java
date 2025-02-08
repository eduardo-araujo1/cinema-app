package com.eduardo.cinema_app.dtos.response;

import com.eduardo.cinema_app.enums.Genre;

public record MovieResponseDTO(
    Long id,
    String title,
    String description,
    Genre genre,
    String posterUrl
) {
}
