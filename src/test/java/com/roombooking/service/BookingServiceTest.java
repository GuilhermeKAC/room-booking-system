package com.roombooking.service;

import com.roombooking.dto.request.BookingRequest;
import com.roombooking.exception.ConflictException;
import com.roombooking.model.Booking;
import com.roombooking.model.Room;
import com.roombooking.model.User;
import com.roombooking.model.enums.BookingStatus;
import com.roombooking.model.enums.RoomStatus;
import com.roombooking.repository.BookingRepository;
import com.roombooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private UserRepository userRepository;
    @Mock private RoomService roomService;

    @InjectMocks private BookingService bookingService;

    private User user;
    private Room room;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("joao").build();
        room = Room.builder().id(1L).name("Sala A").capacity(10).status(RoomStatus.AVAILABLE).build();
    }

    @Test
    void shouldCreateBookingSuccessfully() {
        BookingRequest request = new BookingRequest(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2),
                "Reuniao"
        );

        when(roomService.findById(1L)).thenReturn(room);
        when(userRepository.findByUsername("joao")).thenReturn(Optional.of(user));
        when(bookingRepository.findConflictingBookings(any(), any(), any())).thenReturn(List.of());
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Booking result = bookingService.create(request, "joao");

        assertNotNull(result);
        assertEquals(BookingStatus.CONFIRMED, result.getStatus());
        verify(bookingRepository).save(any());
    }

    @Test
    void shouldThrowConflictWhenRoomIsBooked() {
        BookingRequest request = new BookingRequest(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2),
                "Reuniao"
        );

        when(roomService.findById(1L)).thenReturn(room);
        when(userRepository.findByUsername("joao")).thenReturn(Optional.of(user));
        when(bookingRepository.findConflictingBookings(any(), any(), any()))
                .thenReturn(List.of(new Booking()));

        assertThrows(ConflictException.class, () -> bookingService.create(request, "joao"));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenDurationTooShort() {
        BookingRequest request = new BookingRequest(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusMinutes(20),
                "Curta"
        );

        when(roomService.findById(1L)).thenReturn(room);
        when(userRepository.findByUsername("joao")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> bookingService.create(request, "joao"));
    }

    @Test
    void shouldThrowWhenDurationTooLong() {
        BookingRequest request = new BookingRequest(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(5),
                "Longa"
        );

        when(roomService.findById(1L)).thenReturn(room);
        when(userRepository.findByUsername("joao")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> bookingService.create(request, "joao"));
    }

    @Test
    void shouldThrowWhenRoomIsUnderMaintenance() {
        room.setStatus(RoomStatus.MAINTENANCE);
        BookingRequest request = new BookingRequest(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2),
                "Reuniao"
        );

        when(roomService.findById(1L)).thenReturn(room);
        when(userRepository.findByUsername("joao")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> bookingService.create(request, "joao"));
    }
}
