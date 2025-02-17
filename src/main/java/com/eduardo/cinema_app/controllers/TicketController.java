package com.eduardo.cinema_app.controllers;

import com.eduardo.cinema_app.domain.Ticket;
import com.eduardo.cinema_app.dtos.request.TicketRequestDTO;
import com.eduardo.cinema_app.dtos.response.TicketResponseDTO;
import com.eduardo.cinema_app.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(@RequestBody TicketRequestDTO ticketRequestDTO) {
        var createdTicket = ticketService.createTicket(ticketRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }
}