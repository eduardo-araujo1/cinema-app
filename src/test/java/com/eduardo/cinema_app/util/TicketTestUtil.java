package com.eduardo.cinema_app.util;

import com.eduardo.cinema_app.domain.Customer;
import com.eduardo.cinema_app.domain.Session;
import com.eduardo.cinema_app.domain.Ticket;
import com.eduardo.cinema_app.dtos.request.TicketRequestDTO;
import com.eduardo.cinema_app.dtos.response.TicketResponseDTO;
import com.eduardo.cinema_app.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketTestUtil {

    public static String TICKET_ID = UUID.randomUUID().toString();
    public static Customer CUSTOMER = new Customer("Eduardo Teste","eduardo@teste.com","!Eduardo123");
    public static Session SESSION = SessionTestUtil.createSession();
    public static Status STATUS = Status.AGUARDANDO_PAGAMENTO;
    public static final BigDecimal PRICE = BigDecimal.valueOf(40.00);
    public static LocalDateTime CREATED_AT = LocalDateTime.now();

    public static final List<Long> SEAT_IDS = List.of(1L, 2L, 3L);
    public static final List<String> SEAT_NUMBERS = List.of("1", "2", "3");

    public static Ticket createTicket() {
        Ticket ticket = new Ticket();
        ticket.setId(TICKET_ID);
        ticket.setSession(SESSION);
        ticket.setCustomer(CUSTOMER);
        ticket.setStatus(STATUS);
        ticket.setPrice(PRICE);
        ticket.setCreatedAt(CREATED_AT);
        ticket.setTicketSeats(new ArrayList<>());
        return ticket;
    }

    public static TicketRequestDTO createTicketRequestDTO() {
        return new TicketRequestDTO(
                SESSION.getId(),
                CUSTOMER.getId(),
                SEAT_IDS
        );
    }

    public static TicketResponseDTO createTicketResponseDTO() {
        return new TicketResponseDTO(
                TICKET_ID,
                SEAT_NUMBERS,
                STATUS,
                PRICE,
                CREATED_AT
        );
    }

}
