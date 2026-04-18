package com.roombooking.repository;

import com.roombooking.model.Booking;
import com.roombooking.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b JOIN FETCH b.room JOIN FETCH b.user WHERE b.user.id = :userId ORDER BY b.startTime DESC")
    List<Booking> findByUserIdOrderByStartTimeDesc(@Param("userId") Long userId);

    @Query("SELECT b FROM Booking b JOIN FETCH b.room JOIN FETCH b.user WHERE b.room.id = :roomId AND b.status = :status")
    List<Booking> findByRoomIdAndStatus(@Param("roomId") Long roomId, @Param("status") BookingStatus status);

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
