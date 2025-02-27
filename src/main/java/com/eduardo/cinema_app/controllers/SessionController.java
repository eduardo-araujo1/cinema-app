package com.eduardo.cinema_app.controllers;

import com.eduardo.cinema_app.dtos.request.SessionRequestDTO;
import com.eduardo.cinema_app.dtos.response.SeatResponseDTO;
import com.eduardo.cinema_app.dtos.response.SessionResponseDTO;
import com.eduardo.cinema_app.services.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@Tag(name = "session", description = "Gerencia a criação de sessões e disponibilidade de assentos para uma sessão.")
public class SessionController {

    private final SessionService service;

    public SessionController(SessionService service) {
        this.service = service;
    }

    @Operation(description = "Cria um sessão para determinado filme, (REQUER ROLE_ADMIN)",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sessão criada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Filme ou sala não foi encontrado"),
            @ApiResponse(responseCode = "409", description = "Já existe um sessão para este horário na sala"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<SessionResponseDTO> createSession(@Valid @RequestBody SessionRequestDTO dto) {
        var createSession = service.createSession(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createSession);
    }

    @Operation(description = "Busca assentos disponiveis para uma sessão",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada")
    })

    @GetMapping("/{sessionId}/seats")
    public ResponseEntity<List<SeatResponseDTO>> getAvailableSeats(@PathVariable Long sessionId) {
        List<SeatResponseDTO> seats = service.getAvailableSeats(sessionId);
        return ResponseEntity.ok(seats);
    }
}
