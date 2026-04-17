package com.roombooking.repository;

import com.roombooking.model.Room;
import com.roombooking.model.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByStatus(RoomStatus status);

    boolean existsByName(String name);
}
