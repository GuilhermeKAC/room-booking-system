package com.roombooking.service;

import com.roombooking.dto.response.AvailabilityResponse;
import com.roombooking.dto.response.AvailabilityResponse.Slot;
import com.roombooking.model.Booking;
import com.roombooking.model.Room;
import com.roombooking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private static final LocalTime BUSINESS_START = LocalTime.of(8, 0);
    private static final LocalTime BUSINESS_END   = LocalTime.of(22, 0);
    private static final int SLOT_MINUTES         = 30;

    private final BookingRepository bookingRepository;

    public AvailabilityResponse getAvailability(Room room, LocalDate date) {
        LocalDateTime dayStart = date.atTime(BUSINESS_START);
        LocalDateTime dayEnd   = date.atTime(BUSINESS_END);

        List<Booking> bookings = bookingRepository.findConflictingBookings(
                room.getId(), dayStart, dayEnd
        );

        List<Slot> slots = new ArrayList<>();
        LocalTime cursor = BUSINESS_START;

        while (cursor.plusMinutes(SLOT_MINUTES).compareTo(BUSINESS_END) <= 0) {
            LocalTime slotEnd = cursor.plusMinutes(SLOT_MINUTES);
            LocalDateTime slotStart = date.atTime(cursor);
            LocalDateTime slotEndDt = date.atTime(slotEnd);

            Booking conflict = bookings.stream()
                    .filter(b -> b.getStartTime().isBefore(slotEndDt) && b.getEndTime().isAfter(slotStart))
                    .findFirst()
                    .orElse(null);

            slots.add(new Slot(
                    cursor,
                    slotEnd,
                    conflict == null,
                    conflict != null ? conflict.getUser().getUsername() : null,
                    conflict != null ? conflict.getPurpose() : null
            ));

            cursor = slotEnd;
        }

        return new AvailabilityResponse(room.getId(), room.getName(), date, slots);
    }
}
