package com.roombooking.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AvailabilityResponse(
        Long roomId,
        String roomName,
        LocalDate date,
        List<Slot> slots
) {
    public record Slot(
            LocalTime start,
            LocalTime end,
            boolean available,
            String bookedBy,
            String purpose
    ) {}
}
