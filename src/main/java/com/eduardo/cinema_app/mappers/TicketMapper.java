package com.eduardo.cinema_app.mappers;

import com.eduardo.cinema_app.domain.*;
import com.eduardo.cinema_app.dtos.request.TicketRequestDTO;
import com.eduardo.cinema_app.dtos.response.TicketResponseDTO;
import com.eduardo.cinema_app.enums.Status;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TicketMapper {

    public Ticket toEntity(TicketRequestDTO dto, Customer customer, Session session, BigDecimal price) {
        Ticket ticket = new Ticket();
        ticket.setCustomer(customer);
        ticket.setSession(session);
        ticket.setStatus(Status.AGUARDANDO_PAGAMENTO);
        ticket.setPrice(price);
        return ticket;
    }

    public TicketSeat toTicketSeatEntity(Ticket ticket, Seat seat, Session session) {
        TicketSeat ticketSeat = new TicketSeat();
        ticketSeat.setTicket(ticket);
        ticketSeat.setSeat(seat);
        ticketSeat.setSession(session);

        TicketSeatId ticketSeatId = new TicketSeatId();
        ticketSeatId.setTicketId(ticket.getId());
        ticketSeatId.setSeatId(seat.getId());
        ticketSeat.setId(ticketSeatId);

        return ticketSeat;
    }

    public TicketResponseDTO toDTO(Ticket ticket, List<String> seatNumbers) {
        return new TicketResponseDTO(
                ticket.getId(),
                seatNumbers,
                ticket.getStatus(),
                ticket.getPrice(),
                ticket.getCreatedAt()
        );
    }
}
