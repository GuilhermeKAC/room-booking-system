package com.roombooking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RoomRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull @Min(1) Integer capacity,
        @Size(max = 255) String location,
        boolean hasProjector,
        boolean hasWhiteboard,
        boolean hasAirConditioning
) {}
