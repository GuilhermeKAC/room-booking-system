package com.roombooking.repository;

import com.roombooking.model.Booking;
import com.roombooking.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserIdOrderByStartTimeDesc(Long userId);

    List<Booking> findByRoomIdAndStatus(Long roomId, BookingStatus status);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.room.id = :roomId
              AND b.status = 'CONFIRMED'
              AND b.startTime < :endTime
              AND b.endTime > :startTime
            """)
    List<Booking> findConflictingBookings(
            @Param("roomId") Long roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
