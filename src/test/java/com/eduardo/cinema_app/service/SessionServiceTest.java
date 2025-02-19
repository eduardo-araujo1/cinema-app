package com.eduardo.cinema_app.service;

import com.eduardo.cinema_app.domain.Session;
import com.eduardo.cinema_app.exceptions.MovieNotFoundException;
import com.eduardo.cinema_app.exceptions.RoomNotFoundException;
import com.eduardo.cinema_app.exceptions.SessionAlreadyExistsException;
import com.eduardo.cinema_app.mappers.SessionMapper;
import com.eduardo.cinema_app.repositories.MovieRepository;
import com.eduardo.cinema_app.repositories.RoomRepository;
import com.eduardo.cinema_app.repositories.SeatRepository;
import com.eduardo.cinema_app.repositories.SessionRepository;
import com.eduardo.cinema_app.services.SessionService;
import com.eduardo.cinema_app.util.SessionTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private SessionMapper mapper;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    void createSession_Success() {
        var requestDTO = SessionTestUtil.createSessionRequestDTO();
        var movie = SessionTestUtil.MOVIE;
        var room = SessionTestUtil.ROOM;
        var session = SessionTestUtil.createSession();
        var responseDTO = SessionTestUtil.createSessionResponseDTO();

        when(movieRepository.findByTitleIgnoreCase(requestDTO.movieTitle())).thenReturn(Optional.of(movie));
        when(roomRepository.findByRoomNumber(requestDTO.roomNumber())).thenReturn(Optional.of(room));
        when(sessionRepository.existsOverlappingSession(requestDTO.startTime(), requestDTO.endTime(), room.getId())).thenReturn(false);
        when(mapper.toDomain(requestDTO, movie, room)).thenReturn(session);
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        when(mapper.toDto(session)).thenReturn(responseDTO);


        var result = sessionService.createSession(requestDTO);

        assertEquals(responseDTO, result);
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    void createSession_MovieNotFound() {
        var requestDTO = SessionTestUtil.createSessionRequestDTO();

        when(movieRepository.findByTitleIgnoreCase(requestDTO.movieTitle())).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> sessionService.createSession(requestDTO));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void createSession_RoomNotFound() {
        var requestDTO = SessionTestUtil.createSessionRequestDTO();
        when(movieRepository.findByTitleIgnoreCase(requestDTO.movieTitle())).thenReturn(Optional.of(SessionTestUtil.MOVIE));
        when(roomRepository.findByRoomNumber(requestDTO.roomNumber())).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> sessionService.createSession(requestDTO));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void createSession_SessionAlreadyExists() {
        var requestDTO = SessionTestUtil.createSessionRequestDTO();
        var movie = SessionTestUtil.MOVIE;
        var room = SessionTestUtil.ROOM;

        when(movieRepository.findByTitleIgnoreCase(requestDTO.movieTitle())).thenReturn(Optional.of(movie));
        when(roomRepository.findByRoomNumber(requestDTO.roomNumber())).thenReturn(Optional.of(room));
        when(sessionRepository.existsOverlappingSession(requestDTO.startTime(), requestDTO.endTime(), room.getId())).thenReturn(true);

        assertThrows(SessionAlreadyExistsException.class, () -> sessionService.createSession(requestDTO));
        verify(sessionRepository, never()).save(any(Session.class));
    }
}
