package com.eduardo.cinema_app.service;

import com.eduardo.cinema_app.domain.Room;
import com.eduardo.cinema_app.domain.Seat;
import com.eduardo.cinema_app.dtos.request.RoomRequestDTO;
import com.eduardo.cinema_app.dtos.response.RoomResponseDTO;
import com.eduardo.cinema_app.mappers.RoomMapper;
import com.eduardo.cinema_app.repositories.RoomRepository;
import com.eduardo.cinema_app.repositories.SeatRepository;
import com.eduardo.cinema_app.services.RoomService;
import com.eduardo.cinema_app.util.RoomTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private RoomMapper mapper;

    @InjectMocks
    private RoomService roomService;

    @Test
    void createRoom_Success() {
        RoomRequestDTO requestDTO = RoomTestUtil.createRoomRequestDTO();
        Room roomToSave = RoomTestUtil.createRoom();
        RoomResponseDTO expectedResponse = RoomTestUtil.createRoomResponseDTO();

        when(mapper.toDomain(requestDTO)).thenReturn(roomToSave);
        when(roomRepository.save(roomToSave)).thenReturn(roomToSave);
        when(mapper.toDto(roomToSave)).thenReturn(expectedResponse);

        RoomResponseDTO result = roomService.createRoom(requestDTO);

        assertNotNull(result);
        assertEquals(expectedResponse.id(), result.id());
        assertEquals(expectedResponse.roomNumber(), result.roomNumber());
        assertEquals(expectedResponse.totalSeats(), result.totalSeats());

        verify(roomRepository, times(1)).save(roomToSave);
        verify(seatRepository, times(1)).saveAll(anyList());
    }
}
