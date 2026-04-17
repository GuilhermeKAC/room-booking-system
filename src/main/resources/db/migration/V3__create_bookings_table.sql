CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE bookings (
    id         BIGSERIAL   PRIMARY KEY,
    user_id    BIGINT      NOT NULL,
    room_id    BIGINT      NOT NULL,
    start_time TIMESTAMP   NOT NULL,
    end_time   TIMESTAMP   NOT NULL,
    status     VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
    purpose    TEXT,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_room FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
    CONSTRAINT chk_booking_times CHECK (end_time > start_time),

    CONSTRAINT no_overlap_bookings EXCLUDE USING gist (
        room_id WITH =,
        tsrange(start_time::date + start_time::time, end_time::date + end_time::time) WITH &&
    ) WHERE (status = 'CONFIRMED')
);

CREATE INDEX idx_bookings_user_id   ON bookings(user_id);
CREATE INDEX idx_bookings_room_id   ON bookings(room_id);
CREATE INDEX idx_bookings_status    ON bookings(status);
CREATE INDEX idx_bookings_room_time ON bookings(room_id, start_time, end_time);

CREATE OR REPLACE FUNCTION update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_bookings_updated_at
    BEFORE UPDATE ON bookings
    FOR EACH ROW EXECUTE FUNCTION update_updated_at();
