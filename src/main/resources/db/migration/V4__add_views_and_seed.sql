CREATE INDEX idx_bookings_start_time ON bookings (start_time);

CREATE VIEW active_bookings_today AS
SELECT b.id,
       r.name     AS room_name,
       u.username AS booked_by,
       b.start_time,
       b.end_time,
       b.purpose
FROM bookings b
         JOIN rooms r ON b.room_id = r.id
         JOIN users u ON b.user_id = u.id
WHERE b.status = 'CONFIRMED'
  AND b.start_time::date = CURRENT_DATE
ORDER BY b.start_time;

INSERT INTO rooms (name, capacity, location, has_projector, has_whiteboard)
VALUES ('Sala A', 10, '1º Andar', true, true),
       ('Sala B', 6, 'Térreo', true, false),
       ('Auditório', 50, '2º Andar', true, true),
       ('Sala de Reunião Premium', 8, '1º Andar', true, true);
