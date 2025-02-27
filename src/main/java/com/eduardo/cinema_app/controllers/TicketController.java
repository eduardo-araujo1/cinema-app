package com.eduardo.cinema_app.controllers;

import com.eduardo.cinema_app.dtos.request.TicketRequestDTO;
import com.eduardo.cinema_app.dtos.response.PaymentResponseDTO;
import com.eduardo.cinema_app.services.PaymentService;
import com.eduardo.cinema_app.services.TicketService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@Tag(name = "tickets", description = "Gerencia a reserva de tickets")
public class TicketController {

    private final TicketService ticketService;
    private final PaymentService paymentService;

    public TicketController(TicketService ticketService, PaymentService paymentService) {
        this.ticketService = ticketService;
        this.paymentService = paymentService;
    }

    @Operation(description = "Reserva o ticket e cria a url para fazer o pagamento no STRIPE",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket reservado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario ou sessão não encontrada"),
            @ApiResponse(responseCode = "409", description = "Assento(s) já reservado(s)")
    })

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createTicket(@RequestBody TicketRequestDTO ticketRequestDTO) throws StripeException {
        var createdTicket = ticketService.createTicket(ticketRequestDTO);
        var paymentIntent = paymentService.createPaymentLink(createdTicket);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentIntent);
    }

    @Operation(description = "Caso o pagamento de certo no STRIPE ele redireciona para essa URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento realizado com sucesso")
    })

    @GetMapping("/success/{ticketId}")
    public ResponseEntity<String> handlePaymentSuccess(@PathVariable String ticketId) {
        ticketService.updateTicketStatus(ticketId);
        return ResponseEntity.ok("Pagamento realizado com sucesso!");
    }

    @Operation(description = "Caso de erro no pagamento pelo STRIPE ele redireciona para essa URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Falha no pagamento.")
    })

    @GetMapping("/fail")
    public ResponseEntity<String> handlePaymentFailure(@PathVariable String ticketId) {
        return ResponseEntity.ok("Falha no pagamento.");
    }

}