package com.eduardo.cinema_app.controllers;

import com.eduardo.cinema_app.dtos.request.SessionRequestDTO;
import com.eduardo.cinema_app.dtos.response.SessionResponseDTO;
import com.eduardo.cinema_app.services.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService service;

    public SessionController(SessionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SessionResponseDTO> createSession (@RequestBody SessionRequestDTO dto) {
        var createSession = service.createSession(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createSession);
    }
}
