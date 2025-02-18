package com.eduardo.cinema_app.controllers;

import com.eduardo.cinema_app.dtos.request.SessionRequestDTO;
import com.eduardo.cinema_app.dtos.response.SeatResponseDTO;
import com.eduardo.cinema_app.dtos.response.SessionResponseDTO;
import com.eduardo.cinema_app.services.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService service;

    public SessionController(SessionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SessionResponseDTO> createSession (@Valid @RequestBody SessionRequestDTO dto) {
        var createSession = service.createSession(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createSession);
    }

    @GetMapping("/{sessionId}/seats")
    public ResponseEntity<List<SeatResponseDTO>> getAvailableSeats(@PathVariable Long sessionId) {
        List<SeatResponseDTO> seats = service.getAvailableSeats(sessionId);
        return ResponseEntity.ok(seats);
    }
}
