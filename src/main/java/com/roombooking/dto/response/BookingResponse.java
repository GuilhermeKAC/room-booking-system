package com.roombooking.dto.response;

import com.roombooking.model.Booking;
import com.roombooking.model.enums.BookingStatus;

import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long roomId,
        String roomName,
        String username,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BookingStatus status,
        String purpose
) {
    public static BookingResponse from(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getRoom().getId(),
                booking.getRoom().getName(),
                booking.getUser().getUsername(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                booking.getPurpose()
        );
    }
}
