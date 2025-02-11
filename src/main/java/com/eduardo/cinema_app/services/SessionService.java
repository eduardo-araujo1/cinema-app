package com.eduardo.cinema_app.services;

import com.eduardo.cinema_app.dtos.request.SessionRequestDTO;
import com.eduardo.cinema_app.dtos.response.SessionResponseDTO;
import com.eduardo.cinema_app.mappers.SessionMapper;
import com.eduardo.cinema_app.repositories.MovieRepository;
import com.eduardo.cinema_app.repositories.RoomRepository;
import com.eduardo.cinema_app.repositories.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final SessionMapper mapper;

    public SessionService(SessionRepository sessionRepository, MovieRepository movieRepository, RoomRepository roomRepository, SessionMapper mapper) {
        this.sessionRepository = sessionRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.mapper = mapper;
    }

    @Transactional
    public SessionResponseDTO createSession (SessionRequestDTO dto) {
        var movie = movieRepository.findByTitleIgnoreCase(dto.movieTitle())
                .orElseThrow(() -> new RuntimeException("Filme com o nome " + dto.movieTitle() + "não encontrado."));

        var room = roomRepository.findByRoomNumber(dto.roomNumber())
                .orElseThrow(() -> new RuntimeException("Sala com o número " + dto.roomNumber() + "não encontrada." ));

        checkForOverlappingSessions(dto.startTime(), dto.endTime(), room.getId());

        var session = mapper.toDomain(dto, movie, room);
        var savedSession = sessionRepository.save(session);

        return mapper.toDto(savedSession);
    }

    private void checkForOverlappingSessions(LocalDateTime startTime, LocalDateTime endTime, Long roomId) {
        boolean existsOverlappingSession = sessionRepository.existsOverlappingSession(startTime, endTime, roomId);

        if (existsOverlappingSession) {
            throw new RuntimeException("Já existe uma sessão para este horário na sala com ID " + roomId);
        }
    }
}
