package com.eduardo.cinema_app.controllers;

import com.eduardo.cinema_app.dtos.request.RoomRequestDTO;
import com.eduardo.cinema_app.dtos.response.RoomResponseDTO;
import com.eduardo.cinema_app.services.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
@Tag(name = "room", description = "Gerencia a criação de salas de cinema")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @Operation(description = "Cria uma sala de cinema, (REQUER ROLE_ADMIN)",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sala criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RoomResponseDTO> createRoom(@Valid @RequestBody RoomRequestDTO dto) {
        var savedRoom = service.createRoom(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }
}
