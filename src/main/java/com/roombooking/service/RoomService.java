package com.roombooking.service;

import com.roombooking.dto.request.RoomRequest;
import com.roombooking.exception.ResourceNotFoundException;
import com.roombooking.model.Room;
import com.roombooking.model.enums.RoomStatus;
import com.roombooking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + id));
    }

    public Room create(RoomRequest request) {
        Room room = Room.builder()
                .name(request.name())
                .capacity(request.capacity())
                .location(request.location())
                .hasProjector(request.hasProjector())
                .hasWhiteboard(request.hasWhiteboard())
                .hasAirConditioning(request.hasAirConditioning())
                .build();

        return roomRepository.save(room);
    }

    public Room update(Long id, RoomRequest request) {
        Room room = findById(id);

        room.setName(request.name());
        room.setCapacity(request.capacity());
        room.setLocation(request.location());
        room.setHasProjector(request.hasProjector());
        room.setHasWhiteboard(request.hasWhiteboard());
        room.setHasAirConditioning(request.hasAirConditioning());

        return roomRepository.save(room);
    }

    public void delete(Long id) {
        Room room = findById(id);
        roomRepository.delete(room);
    }

    public List<Room> findAvailable() {
        return roomRepository.findByStatus(RoomStatus.AVAILABLE);
    }
}
