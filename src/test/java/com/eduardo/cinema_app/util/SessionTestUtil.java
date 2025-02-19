package com.eduardo.cinema_app.util;

import com.eduardo.cinema_app.domain.Movie;
import com.eduardo.cinema_app.domain.Room;
import com.eduardo.cinema_app.domain.Session;
import com.eduardo.cinema_app.dtos.request.SessionRequestDTO;
import com.eduardo.cinema_app.dtos.response.SessionResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SessionTestUtil {

    public static Long ID = 1L;
    public static Movie MOVIE = MovieTestUtil.createMovie();
    public static Room ROOM = RoomTestUtil.createRoom();
    public static final LocalDateTime START_TIME = LocalDateTime.now().plusDays(1);
    public static final LocalDateTime END_TIME = START_TIME.plusHours(2);
    public static final BigDecimal PRICE_PER_SEAT = BigDecimal.valueOf(20.00);

    public static Session createSession() {
        Session session = new Session();
        session.setId(ID);
        session.setMovie(MOVIE);
        session.setRoom(ROOM);
        session.setStartTime(START_TIME);
        session.setEndTime(END_TIME);
        session.setPricePerSeat(PRICE_PER_SEAT);
        session.setTicketSeats(new ArrayList<>());
        return session;
    }

    public static SessionRequestDTO createSessionRequestDTO() {
        return new SessionRequestDTO(
                MOVIE.getTitle(),
                ROOM.getRoomNumber(),
                START_TIME,
                END_TIME,
                PRICE_PER_SEAT
        );
    }

    public static SessionResponseDTO createSessionResponseDTO() {
        return new SessionResponseDTO(
                ID,
                MovieTestUtil.createMovieResponseDTO(),
                RoomTestUtil.createRoomResponseDTO(),
                START_TIME,
                END_TIME,
                PRICE_PER_SEAT
        );
    }
}
