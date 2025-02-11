package com.eduardo.cinema_app.repositories;

import com.eduardo.cinema_app.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query("SELECT COUNT(s) > 0 FROM Session s " +
            "WHERE s.room.id = :roomId " +
            "AND (:startTime < s.endTime AND :endTime > s.startTime)")
    boolean existsOverlappingSession(
            @Param("startTime")LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("roomId") Long roomId
            );

}
