package com.roombooking.dto.response;

import com.roombooking.model.Room;
import com.roombooking.model.enums.RoomStatus;

public record RoomResponse(
        Long id,
        String name,
        int capacity,
        String location,
        boolean hasProjector,
        boolean hasWhiteboard,
        boolean hasAirConditioning,
        RoomStatus status
) {
    public static RoomResponse from(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getCapacity(),
                room.getLocation(),
                room.isHasProjector(),
                room.isHasWhiteboard(),
                room.isHasAirConditioning(),
                room.getStatus()
        );
    }
}
