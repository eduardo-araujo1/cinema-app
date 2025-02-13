package com.eduardo.cinema_app.mappers;

import com.eduardo.cinema_app.domain.Movie;
import com.eduardo.cinema_app.domain.Room;
import com.eduardo.cinema_app.domain.Session;
import com.eduardo.cinema_app.dtos.request.SessionRequestDTO;
import com.eduardo.cinema_app.dtos.response.MovieSessionDTO;
import com.eduardo.cinema_app.dtos.response.SessionResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    private final MovieMapper movieMapper;
    private final RoomMapper roomMapper;

    public SessionMapper(MovieMapper movieMapper, RoomMapper roomMapper) {
        this.movieMapper = movieMapper;
        this.roomMapper = roomMapper;
    }

    public Session toDomain(SessionRequestDTO dto, Movie movie, Room room) {
        Session session = new Session();
        session.setMovie(movie);
        session.setRoom(room);
        session.setStartTime(dto.startTime());
        session.setEndTime(dto.endTime());
        return session;
    }

    public SessionResponseDTO toDto (Session session) {
        return new SessionResponseDTO(
                session.getId(),
                movieMapper.toDto(session.getMovie()),
                roomMapper.toDto(session.getRoom()),
                session.getStartTime(),
                session.getEndTime()
        );
    }

    public MovieSessionDTO toMovieSessionDTO(Session session) {
        return new MovieSessionDTO(
                session.getId(),
                session.getStartTime(),
                session.getEndTime()
        );
    }
}
