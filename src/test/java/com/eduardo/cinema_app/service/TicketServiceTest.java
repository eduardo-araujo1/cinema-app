package com.eduardo.cinema_app.service;

import com.eduardo.cinema_app.domain.*;
import com.eduardo.cinema_app.dtos.request.TicketRequestDTO;
import com.eduardo.cinema_app.dtos.response.TicketResponseDTO;
import com.eduardo.cinema_app.enums.Status;
import com.eduardo.cinema_app.exceptions.SeatOccupiedException;
import com.eduardo.cinema_app.exceptions.TicketNotFoundException;
import com.eduardo.cinema_app.exceptions.UnableToLockSeatsException;
import com.eduardo.cinema_app.mappers.TicketMapper;
import com.eduardo.cinema_app.repositories.*;
import com.eduardo.cinema_app.services.EmailService;
import com.eduardo.cinema_app.services.TicketService;
import com.eduardo.cinema_app.util.TicketTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.PessimisticLockingFailureException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private TicketMapper ticketMapper;
    @Mock
    private TicketSeatRepository ticketSeatRepository;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private TicketService ticketService;


    @Test
    void createTicket_WithValidData_ShouldReturnTicketResponseDTO() {
        TicketRequestDTO requestDTO = TicketTestUtil.createTicketRequestDTO();
        Customer customer = TicketTestUtil.CUSTOMER;
        Session session = TicketTestUtil.SESSION;
        Ticket ticket = TicketTestUtil.createTicket();
        List<Seat> seats = createMockSeats();
        List<TicketSeat> ticketSeats = createMockTicketSeats(ticket, seats, session);
        TicketResponseDTO expectedResponse = TicketTestUtil.createTicketResponseDTO();

        BigDecimal pricePerSeat = session.getPricePerSeat();
        BigDecimal totalPrice = pricePerSeat.multiply(BigDecimal.valueOf(TicketTestUtil.SEAT_IDS.size()));

        when(customerRepository.findById(requestDTO.customerId()))
                .thenReturn(Optional.of(customer));
        when(sessionRepository.findById(requestDTO.sessionId()))
                .thenReturn(Optional.of(session));
        when(seatRepository.findAvailableSeatsWithLock(TicketTestUtil.SEAT_IDS, session.getId()))
                .thenReturn(seats);
        when(ticketMapper.toEntity(eq(requestDTO), eq(customer), eq(session), eq(totalPrice)))
                .thenReturn(ticket);
        when(ticketRepository.saveAndFlush(ticket))
                .thenReturn(ticket);
        when(ticketMapper.toTicketSeatEntity(any(), any(), any()))
                .thenReturn(new TicketSeat());
        when(ticketMapper.toDTO(ticket, TicketTestUtil.SEAT_NUMBERS))
                .thenReturn(expectedResponse);


        TicketResponseDTO result = ticketService.createTicket(requestDTO);

        assertNotNull(result);
        assertEquals(expectedResponse.id(), result.id());
        assertEquals(expectedResponse.seatNumbers(), result.seatNumbers());
        assertEquals(expectedResponse.status(), result.status());
        assertEquals(expectedResponse.price(), result.price());

        verify(customerRepository).findById(requestDTO.customerId());
        verify(sessionRepository).findById(requestDTO.sessionId());
        verify(seatRepository).findAvailableSeatsWithLock(TicketTestUtil.SEAT_IDS, session.getId());
        verify(ticketRepository).saveAndFlush(ticket);
    }

    @Test
    void createTicket_WhenSeatsAreOccupied_ShouldThrowSeatOccupiedException() {
        TicketRequestDTO requestDTO = TicketTestUtil.createTicketRequestDTO();
        Customer customer = TicketTestUtil.CUSTOMER;
        Session session = TicketTestUtil.SESSION;

        List<Seat> availableSeats = createMockSeats().subList(0, 2);

        when(customerRepository.findById(requestDTO.customerId()))
                .thenReturn(Optional.of(customer));
        when(sessionRepository.findById(requestDTO.sessionId()))
                .thenReturn(Optional.of(session));
        when(seatRepository.findAvailableSeatsWithLock(TicketTestUtil.SEAT_IDS, session.getId()))
                .thenReturn(availableSeats);


        assertThrows(SeatOccupiedException.class, () -> ticketService.createTicket(requestDTO));
        verify(customerRepository).findById(requestDTO.customerId());
        verify(sessionRepository).findById(requestDTO.sessionId());
        verify(seatRepository).findAvailableSeatsWithLock(TicketTestUtil.SEAT_IDS, session.getId());
        verifyNoInteractions(ticketRepository, ticketSeatRepository);
    }

    @Test
    void createTicket_WhenPessimisticLockFails_ShouldThrowUnableToLockSeatsException() {
        TicketRequestDTO requestDTO = TicketTestUtil.createTicketRequestDTO();
        Customer customer = TicketTestUtil.CUSTOMER;
        Session session = TicketTestUtil.SESSION;

        when(customerRepository.findById(requestDTO.customerId()))
                .thenReturn(Optional.of(customer));
        when(sessionRepository.findById(requestDTO.sessionId()))
                .thenReturn(Optional.of(session));
        when(seatRepository.findAvailableSeatsWithLock(TicketTestUtil.SEAT_IDS, session.getId()))
                .thenThrow(new PessimisticLockingFailureException("Lock failed"));


        assertThrows(UnableToLockSeatsException.class, () -> ticketService.createTicket(requestDTO));
        verify(customerRepository).findById(requestDTO.customerId());
        verify(sessionRepository).findById(requestDTO.sessionId());
        verify(seatRepository).findAvailableSeatsWithLock(TicketTestUtil.SEAT_IDS, session.getId());
        verifyNoInteractions(ticketRepository, ticketSeatRepository);
    }

    private List<Seat> createMockSeats() {
        return TicketTestUtil.SEAT_IDS.stream()
                .map(id -> {
                    Seat seat = new Seat();
                    seat.setId(id);
                    seat.setSeatNumber(id.toString());
                    return seat;
                })
                .collect(Collectors.toList());
    }

    private List<TicketSeat> createMockTicketSeats(Ticket ticket, List<Seat> seats, Session session) {
        return seats.stream()
                .map(seat -> {
                    TicketSeat ticketSeat = new TicketSeat();
                    ticketSeat.setTicket(ticket);
                    ticketSeat.setSeat(seat);
                    ticketSeat.setSession(session);
                    return ticketSeat;
                })
                .collect(Collectors.toList());
    }

    @Test
    void updateTicketStatus_WithValidTicketId_ShouldUpdateStatusAndSendEmail() {
        Ticket ticket = TicketTestUtil.createTicket();
        ticket.setStatus(Status.RESERVADO);

        when(ticketRepository.findByIdWithDetails(ticket.getId()))
                .thenReturn(Optional.of(ticket));

        ticketService.updateTicketStatus(ticket.getId());

        assertEquals(Status.RESERVADO, ticket.getStatus());
        verify(ticketRepository).findByIdWithDetails(ticket.getId());
        verify(ticketRepository).save(ticket);
        verify(emailService).sendPaymentConfirmationEmail(ticket);
    }

    @Test
    void updateTicketStatus_WithInvalidTicketId_ShouldThrowException() {
        String invalidTicketId = "invalid-uuid";

        when(ticketRepository.findByIdWithDetails(invalidTicketId))
                .thenReturn(Optional.empty());


        TicketNotFoundException exception = assertThrows(TicketNotFoundException.class, () ->
                ticketService.updateTicketStatus(invalidTicketId)
        );

        assertEquals("Ticket não encontrado", exception.getMessage());
        verify(ticketRepository).findByIdWithDetails(invalidTicketId);
        verifyNoMoreInteractions(ticketRepository, emailService);
    }

}
