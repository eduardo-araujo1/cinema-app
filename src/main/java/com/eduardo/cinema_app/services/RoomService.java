package com.eduardo.cinema_app.services;

import com.eduardo.cinema_app.domain.Seat;
import com.eduardo.cinema_app.dtos.request.RoomRequestDTO;
import com.eduardo.cinema_app.dtos.response.RoomResponseDTO;
import com.eduardo.cinema_app.mappers.RoomMapper;
import com.eduardo.cinema_app.repositories.RoomRepository;
import com.eduardo.cinema_app.repositories.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    private RoomRepository roomRepository;
    private SeatRepository seatRepository;
    private RoomMapper mapper;

    public RoomService(RoomRepository repository, SeatRepository seatRepository, RoomMapper mapper) {
        this.roomRepository = repository;
        this.seatRepository = seatRepository;
        this.mapper = mapper;
    }

    public RoomResponseDTO createRoom(RoomRequestDTO dto) {
        var roomTosave = mapper.toDomain(dto);
        var savedRoom = roomRepository.save(roomTosave);

        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= savedRoom.getTotalSeats(); i++) {
            Seat seat = new Seat();
            seat.setRoom(savedRoom);
            seat.setSeatNumber(String.valueOf(i));
            seats.add(seat);
        }
        seatRepository.saveAll(seats);

        return mapper.toDto(savedRoom);
    }

}
