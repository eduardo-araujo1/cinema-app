package com.eduardo.cinema_app.controllers;

import com.eduardo.cinema_app.domain.Room;
import com.eduardo.cinema_app.dtos.request.RoomRequestDTO;
import com.eduardo.cinema_app.dtos.response.RoomResponseDTO;
import com.eduardo.cinema_app.services.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RoomResponseDTO> createRoom (@Valid @RequestBody RoomRequestDTO dto) {
        var savedRoom = service.createRoom(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }
}
