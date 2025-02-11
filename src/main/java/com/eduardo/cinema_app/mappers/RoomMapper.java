package com.eduardo.cinema_app.mappers;

import com.eduardo.cinema_app.domain.Room;
import com.eduardo.cinema_app.dtos.request.RoomRequestDTO;
import com.eduardo.cinema_app.dtos.response.RoomResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public Room toDomain(RoomRequestDTO dto) {
        Room room = new Room();
        room.setRoomNumber(dto.roomNumber());
        room.setTotalSeats(dto.totalSeats());
        return room;
    }

    public RoomResponseDTO toDto(Room room) {
        return new RoomResponseDTO(
                room.getId(),
                room.getRoomNumber(),
                room.getTotalSeats()
        );
    }
}
