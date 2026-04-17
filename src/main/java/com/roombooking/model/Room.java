package com.roombooking.model;

import com.roombooking.model.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @Column(length = 255)
    private String location;

    @Column(name = "has_projector", nullable = false)
    @Builder.Default
    private boolean hasProjector = false;

    @Column(name = "has_whiteboard", nullable = false)
    @Builder.Default
    private boolean hasWhiteboard = false;

    @Column(name = "has_air_conditioning", nullable = false)
    @Builder.Default
    private boolean hasAirConditioning = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private RoomStatus status = RoomStatus.AVAILABLE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
