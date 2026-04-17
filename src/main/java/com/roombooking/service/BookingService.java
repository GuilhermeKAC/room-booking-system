package com.roombooking.service;

import com.roombooking.dto.request.BookingRequest;
import com.roombooking.exception.ConflictException;
import com.roombooking.exception.ResourceNotFoundException;
import com.roombooking.model.Booking;
import com.roombooking.model.Room;
import com.roombooking.model.User;
import com.roombooking.model.enums.BookingStatus;
import com.roombooking.model.enums.RoomStatus;
import com.roombooking.repository.BookingRepository;
import com.roombooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private static final int MIN_DURATION_MINUTES = 30;
    private static final int MAX_DURATION_HOURS   = 4;
    private static final int MIN_CANCEL_HOURS     = 2;

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomService roomService;

    @Transactional
    public Booking create(BookingRequest request, String username) {
        Room room = roomService.findById(request.roomId());
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        validate(request, room);

        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                room.getId(), request.startTime(), request.endTime()
        );

        if (!conflicts.isEmpty()) {
            throw new ConflictException("Room is already booked for this time slot");
        }

        Booking booking = Booking.builder()
                .user(user)
                .room(room)
                .startTime(request.startTime())
                .endTime(request.endTime())
                .purpose(request.purpose())
                .build();

        return bookingRepository.save(booking);
    }

    public List<Booking> findByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return bookingRepository.findByUserIdOrderByStartTimeDesc(user.getId());
    }

    public List<Booking> findByRoom(Long roomId) {
        roomService.findById(roomId);
        return bookingRepository.findByRoomIdAndStatus(roomId, BookingStatus.CONFIRMED);
    }

    @Transactional
    public void cancel(Long id, String username) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));

        if (!booking.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only cancel your own bookings");
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalArgumentException("Only confirmed bookings can be cancelled");
        }

        long hoursUntilStart = Duration.between(LocalDateTime.now(), booking.getStartTime()).toHours();
        if (hoursUntilStart < MIN_CANCEL_HOURS) {
            throw new IllegalArgumentException("Bookings must be cancelled at least " + MIN_CANCEL_HOURS + " hours in advance");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    private void validate(BookingRequest request, Room room) {
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new IllegalArgumentException("Room is not available for booking");
        }

        if (!request.endTime().isAfter(request.startTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        long minutes = Duration.between(request.startTime(), request.endTime()).toMinutes();

        if (minutes < MIN_DURATION_MINUTES) {
            throw new IllegalArgumentException("Minimum booking duration is " + MIN_DURATION_MINUTES + " minutes");
        }

        if (minutes > MAX_DURATION_HOURS * 60L) {
            throw new IllegalArgumentException("Maximum booking duration is " + MAX_DURATION_HOURS + " hours");
        }
    }
}
