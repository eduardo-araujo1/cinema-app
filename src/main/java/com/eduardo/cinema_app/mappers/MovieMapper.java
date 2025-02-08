package com.eduardo.cinema_app.mappers;

import com.eduardo.cinema_app.domain.Movie;
import com.eduardo.cinema_app.dtos.request.MovieRequestDTO;
import com.eduardo.cinema_app.dtos.response.MovieResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public Movie toDomain(MovieRequestDTO dto, String imageUrl) {
        Movie movie = new Movie();
        movie.setTitle(dto.title());
        movie.setDescription(dto.description());
        movie.setGenre(dto.genre());
        movie.setPosterUrl(imageUrl);
        return movie;
    }

    public MovieResponseDTO toDto(Movie movie){
        return new MovieResponseDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getGenre(),
                movie.getPosterUrl()
        );
    }
}
