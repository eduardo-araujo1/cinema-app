package com.eduardo.cinema_app.controllers;

import com.eduardo.cinema_app.domain.Ticket;
import com.eduardo.cinema_app.dtos.request.TicketRequestDTO;
import com.eduardo.cinema_app.dtos.response.PaymentResponseDTO;
import com.eduardo.cinema_app.dtos.response.TicketResponseDTO;
import com.eduardo.cinema_app.services.PaymentService;
import com.eduardo.cinema_app.services.TicketService;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final PaymentService paymentService;

    public TicketController(TicketService ticketService, PaymentService paymentService) {
        this.ticketService = ticketService;
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createTicket(@RequestBody TicketRequestDTO ticketRequestDTO) throws StripeException {
        var createdTicket = ticketService.createTicket(ticketRequestDTO);
        var paymentIntent = paymentService.createPaymentLink(createdTicket);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentIntent);
    }

    @GetMapping("/success/{ticketId}")
    public ResponseEntity<String> handlePaymentSuccess(@PathVariable String ticketId) {
        ticketService.updateTicketStatus(ticketId);
        return ResponseEntity.ok("Pagamento realizado com sucesso!");
    }


}