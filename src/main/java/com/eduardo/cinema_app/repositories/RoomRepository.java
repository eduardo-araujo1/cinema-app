package com.eduardo.cinema_app.repositories;

import com.eduardo.cinema_app.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomNumber(Integer roomNumber);
}
