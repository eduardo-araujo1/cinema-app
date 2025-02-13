package com.eduardo.cinema_app.controllers;

import com.eduardo.cinema_app.dtos.request.MovieRequestDTO;
import com.eduardo.cinema_app.dtos.response.MovieResponseDTO;
import com.eduardo.cinema_app.dtos.response.MovieWithSessionsDTO;
import com.eduardo.cinema_app.enums.Genre;
import com.eduardo.cinema_app.services.MovieService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> createMovie(@Valid MovieRequestDTO dto,
                                              @RequestPart(value = "image", required = true) MultipartFile image) {
        service.createMovie(dto,image);
        return ResponseEntity.status(HttpStatus.CREATED).body("Filme criado com sucesso.");
    }

    @GetMapping
    public ResponseEntity<Page<MovieResponseDTO>> getMovies(@RequestParam(required = false) String title,
                                                           @RequestParam(required = false) Genre genre,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {

        Page<MovieResponseDTO> movies = service.findMoviesWithFilters(title, genre, page, size);
        return ResponseEntity.ok(movies);
    }


    @GetMapping("/{id}/sessions")
    public ResponseEntity<MovieWithSessionsDTO> getMovieWithSessions(@PathVariable Long id) {
        var moviesWithSessions = service.findMovieWithSessions(id);
        return ResponseEntity.ok(moviesWithSessions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponseDTO> updateMovie(
            @PathVariable Long id,
            MovieRequestDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        MovieResponseDTO updatedMovie = service.updateMovie(id, dto, image);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        service.deleteMovie(id);
        return ResponseEntity.status(HttpStatus.OK).body("Filme deletado com sucesso.");
    }
}
