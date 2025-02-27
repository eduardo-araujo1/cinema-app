package com.eduardo.cinema_app.controllers;

import com.eduardo.cinema_app.dtos.request.MovieRequestDTO;
import com.eduardo.cinema_app.dtos.response.MovieResponseDTO;
import com.eduardo.cinema_app.dtos.response.MovieWithSessionsDTO;
import com.eduardo.cinema_app.enums.Genre;
import com.eduardo.cinema_app.services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/movies")
@Tag(name = "movie", description = "Gerencia o cadastro, consulta e manutenção de filmes.")
public class MovieController {

    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @Operation(description = "Cadastra um novo filme (REQUER ROLE_ADMIN)",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Filme criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> createMovie(@Valid MovieRequestDTO dto,
                                              @RequestPart(value = "image", required = true) MultipartFile image) {
        service.createMovie(dto, image);
        return ResponseEntity.status(HttpStatus.CREATED).body("Filme criado com sucesso.");
    }

    @Operation(description = "Retorna uma lista paginada de filmes podendo filtrar por titulo e genero")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de filmes retornada com sucesso")
    })

    @GetMapping
    public ResponseEntity<Page<MovieResponseDTO>> getMovies(@RequestParam(required = false) String title,
                                                            @RequestParam(required = false) Genre genre,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {

        Page<MovieResponseDTO> movies = service.findMoviesWithFilters(title, genre, page, size);
        return ResponseEntity.ok(movies);
    }

    @Operation(description = "Retorna detalhes do filme e as sessões agendadas",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filme encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado.")
    })


    @GetMapping("/{id}/sessions")
    public ResponseEntity<MovieWithSessionsDTO> getMovieWithSessions(@PathVariable Long id) {
        var moviesWithSessions = service.findMovieWithSessions(id);
        return ResponseEntity.ok(moviesWithSessions);
    }

    @Operation(description = "Atualiza um filme existente, (REQUER ROLE_ADMIN)",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filme atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado")
    })

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<MovieResponseDTO> updateMovie(
            @PathVariable Long id,
            MovieRequestDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        MovieResponseDTO updatedMovie = service.updateMovie(id, dto, image);
        return ResponseEntity.ok(updatedMovie);
    }

    @Operation(description = "Exclui um filme e todas as suas sessões associadas, (REQUER ROLE_ADMIN)",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filme excluido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao excluir arquivo da imagem")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        service.deleteMovie(id);
        return ResponseEntity.status(HttpStatus.OK).body("Filme deletado com sucesso.");
    }
}
