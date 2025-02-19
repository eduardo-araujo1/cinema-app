package com.eduardo.cinema_app.util;

import com.eduardo.cinema_app.domain.Room;
import com.eduardo.cinema_app.dtos.request.RoomRequestDTO;
import com.eduardo.cinema_app.dtos.response.RoomResponseDTO;

public class RoomTestUtil {

    public static Long ROOM_ID = 1L;
    public static Integer ROOM_NUMBER = 1;
    public static Integer TOTAL_SEATS = 50;

    public static Room createRoom() {
        Room room = new Room();
        room.setId(ROOM_ID);
        room.setRoomNumber(ROOM_NUMBER);
        room.setTotalSeats(TOTAL_SEATS);
        return room;
    }

    public static RoomRequestDTO createRoomRequestDTO() {
        return new RoomRequestDTO(ROOM_NUMBER,TOTAL_SEATS);
    }

    public static RoomResponseDTO createRoomResponseDTO() {
        return new RoomResponseDTO(ROOM_ID,ROOM_NUMBER,TOTAL_SEATS);
    }
}
