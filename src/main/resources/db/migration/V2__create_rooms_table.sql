CREATE TABLE rooms
(
    id                   BIGSERIAL PRIMARY KEY,
    name                 VARCHAR(100) NOT NULL,
    capacity             INT          NOT NULL CHECK (capacity > 0),
    location             VARCHAR(255),
    has_projector        BOOLEAN      NOT NULL DEFAULT false,
    has_whiteboard       BOOLEAN      NOT NULL DEFAULT false,
    has_air_conditioning BOOLEAN      NOT NULL DEFAULT false,
    status               VARCHAR(20)  NOT NULL DEFAULT 'AVAILABLE',
    created_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_rooms_status ON rooms (status);
CREATE INDEX idx_rooms_capacity ON rooms (capacity);
