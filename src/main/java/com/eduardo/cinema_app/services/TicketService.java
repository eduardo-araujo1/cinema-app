package com.eduardo.cinema_app.services;

import com.eduardo.cinema_app.domain.*;
import com.eduardo.cinema_app.dtos.request.TicketRequestDTO;
import com.eduardo.cinema_app.dtos.response.TicketResponseDTO;
import com.eduardo.cinema_app.enums.Status;
import com.eduardo.cinema_app.exceptions.CustomerNotFoundException;
import com.eduardo.cinema_app.exceptions.SeatOccupiedException;
import com.eduardo.cinema_app.exceptions.SessionNotFoundException;
import com.eduardo.cinema_app.exceptions.UnableToLockSeatsException;
import com.eduardo.cinema_app.mappers.TicketMapper;
import com.eduardo.cinema_app.repositories.*;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final SessionRepository sessionRepository;
    private final SeatRepository seatRepository;
    private final CustomerRepository customerRepository;
    private final TicketMapper ticketMapper;
    private final TicketSeatRepository ticketSeatRepository;
    private final EmailService emailService;

    public TicketService(TicketRepository ticketRepository,
                         SessionRepository sessionRepository,
                         SeatRepository seatRepository,
                         CustomerRepository customerRepository,
                         TicketMapper ticketMapper, TicketSeatRepository ticketSeatRepository, EmailService emailService) {
        this.ticketRepository = ticketRepository;
        this.sessionRepository = sessionRepository;
        this.seatRepository = seatRepository;
        this.customerRepository = customerRepository;
        this.ticketMapper = ticketMapper;
        this.ticketSeatRepository = ticketSeatRepository;
        this.emailService = emailService;
    }


    @Transactional
    public TicketResponseDTO createTicket(TicketRequestDTO ticketDTO) {
        try {
            Customer customer = findCustomerById(ticketDTO.customerId());
            Session session = findSessionById(ticketDTO.sessionId());

            List<Seat> availableSeats = fetchAndValidateSeats(ticketDTO.seatsId(), session);

            Ticket ticket = createAndSaveTicket(ticketDTO, customer, session, availableSeats.size());

            List<String> seatNumbers = mapAndSaveTicketSeats(ticket, availableSeats, session);

            return ticketMapper.toDTO(ticket, seatNumbers);

        } catch (PessimisticLockingFailureException e) {
            throw new UnableToLockSeatsException("Não é possível bloquear os assentos. Tente novamente.", e);
        }
    }

    private Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Usuário não encontrado."));
    }

    private Session findSessionById(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Sessão não encontrada."));
    }

    private List<Seat> fetchAndValidateSeats(List<Long> seatIds, Session session) {
        List<Seat> availableSeats = seatRepository.findAvailableSeatsWithLock(seatIds, session.getId());

        if (availableSeats.size() != seatIds.size()) {
            throw new SeatOccupiedException("Um ou mais assentos já estão ocupados.");
        }
        return availableSeats;
    }

    private Ticket createAndSaveTicket(TicketRequestDTO ticketDTO, Customer customer, Session session, int numberOfSeats) {
        BigDecimal totalPrice = calculateTicketPrice(session, numberOfSeats);
        Ticket ticket = ticketMapper.toEntity(ticketDTO, customer, session, totalPrice);
        ticket = ticketRepository.saveAndFlush(ticket);
        return ticket;
    }

    private List<String> mapAndSaveTicketSeats(Ticket ticket, List<Seat> availableSeats, Session session) {
        List<TicketSeat> ticketSeats = createTicketSeats(ticket, availableSeats, session);
        ticketSeatRepository.saveAll(ticketSeats);
        return extractSeatNumbers(availableSeats);
    }

    private List<TicketSeat> createTicketSeats(Ticket ticket, List<Seat> seats, Session session) {
        return seats.stream()
                .map(seat -> ticketMapper.toTicketSeatEntity(ticket, seat, session))
                .toList();
    }

    private List<String> extractSeatNumbers(List<Seat> seats) {
        return seats.stream()
                .map(Seat::getSeatNumber)
                .toList();
    }

    private BigDecimal calculateTicketPrice(Session session, int numberOfSeats) {
        return session.getPricePerSeat().multiply(BigDecimal.valueOf(numberOfSeats));
    }

    @Transactional
    public void updateTicketStatus(String ticketId) {
        Ticket ticket = ticketRepository.findByIdWithDetails(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));
        ticket.setStatus(Status.RESERVADO);
        ticketRepository.save(ticket);

        emailService.sendPaymentConfirmationEmail(ticket);
    }
}