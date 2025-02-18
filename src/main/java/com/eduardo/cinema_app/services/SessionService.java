package com.eduardo.cinema_app.services;

import com.eduardo.cinema_app.domain.Seat;
import com.eduardo.cinema_app.dtos.request.SessionRequestDTO;
import com.eduardo.cinema_app.dtos.response.SeatResponseDTO;
import com.eduardo.cinema_app.dtos.response.SessionResponseDTO;
import com.eduardo.cinema_app.exceptions.MovieNotFoundException;
import com.eduardo.cinema_app.exceptions.RoomNotFoundException;
import com.eduardo.cinema_app.exceptions.SessionAlreadyExistsException;
import com.eduardo.cinema_app.exceptions.SessionNotFoundException;
import com.eduardo.cinema_app.mappers.SessionMapper;
import com.eduardo.cinema_app.repositories.MovieRepository;
import com.eduardo.cinema_app.repositories.RoomRepository;
import com.eduardo.cinema_app.repositories.SeatRepository;
import com.eduardo.cinema_app.repositories.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final SessionMapper mapper;
    private final SeatRepository seatRepository;

    public SessionService(SessionRepository sessionRepository, MovieRepository movieRepository, RoomRepository roomRepository, SessionMapper mapper, SeatRepository seatRepository) {
        this.sessionRepository = sessionRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.mapper = mapper;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public SessionResponseDTO createSession(SessionRequestDTO dto) {
        var movie = movieRepository.findByTitleIgnoreCase(dto.movieTitle())
                .orElseThrow(() -> new MovieNotFoundException("Filme com o nome " + dto.movieTitle() + " não encontrado."));

        var room = roomRepository.findByRoomNumber(dto.roomNumber())
                .orElseThrow(() -> new RoomNotFoundException("Sala com o número " + dto.roomNumber() + " não encontrada."));

        checkForOverlappingSessions(dto.startTime(), dto.endTime(), room.getId());

        var session = mapper.toDomain(dto, movie, room);
        var savedSession = sessionRepository.save(session);

        return mapper.toDto(savedSession);
    }

    private void checkForOverlappingSessions(LocalDateTime startTime, LocalDateTime endTime, Long roomId) {
        boolean existsOverlappingSession = sessionRepository.existsOverlappingSession(startTime, endTime, roomId);

        if (existsOverlappingSession) {
            throw new SessionAlreadyExistsException("Já existe uma sessão para este horário na sala com ID " + roomId);
        }
    }

    public List<SeatResponseDTO> getAvailableSeats(Long sessionId) {
        var session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Sessão com ID " + sessionId + " não encontrada."));

        List<Seat> seats = seatRepository.findByRoomId(session.getRoom().getId());

        return seats.stream()
                .map(seat -> new SeatResponseDTO(seat.getId(), seat.getSeatNumber(), seat.getAvailable()))
                .collect(Collectors.toList());
    }


}
