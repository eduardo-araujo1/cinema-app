package com.eduardo.cinema_app.repositories;

import com.eduardo.cinema_app.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
