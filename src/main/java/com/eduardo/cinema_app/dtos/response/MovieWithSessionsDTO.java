package com.eduardo.cinema_app.dtos.response;

import com.eduardo.cinema_app.enums.Genre;

import java.util.List;

public record MovieWithSessionsDTO(
        Long id,
        String title,
        String description,
        Genre genre,
        String posterUrl,
        List<MovieSessionDTO> sessions
) {
}
