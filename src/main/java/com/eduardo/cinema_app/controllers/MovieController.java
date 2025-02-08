package com.eduardo.cinema_app.controllers;

import com.eduardo.cinema_app.dtos.request.MovieRequestDTO;
import com.eduardo.cinema_app.services.MovieService;
import jakarta.validation.Valid;
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
}
