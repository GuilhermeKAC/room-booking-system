package com.roombooking.controller;

import com.roombooking.dto.request.BookingRequest;
import com.roombooking.dto.response.AvailabilityResponse;
import com.roombooking.dto.response.BookingResponse;
import com.roombooking.model.Room;
import com.roombooking.service.AvailabilityService;
import com.roombooking.service.BookingService;
import com.roombooking.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final AvailabilityService availabilityService;
    private final RoomService roomService;

    @PostMapping("/api/bookings")
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody BookingRequest request,
                                                   Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BookingResponse.from(bookingService.create(request, authentication.getName())));
    }

    @GetMapping("/api/bookings/me")
    public List<BookingResponse> myBookings(Authentication authentication) {
        return bookingService.findByUser(authentication.getName()).stream()
                .map(BookingResponse::from)
                .toList();
    }

    @GetMapping("/api/bookings/room/{roomId}")
    public List<BookingResponse> byRoom(@PathVariable Long roomId) {
        return bookingService.findByRoom(roomId).stream()
                .map(BookingResponse::from)
                .toList();
    }

    @DeleteMapping("/api/bookings/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id, Authentication authentication) {
        bookingService.cancel(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/rooms/{id}/availability")
    public AvailabilityResponse availability(@PathVariable Long id,
                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Room room = roomService.findById(id);
        return availabilityService.getAvailability(room, date);
    }
}
