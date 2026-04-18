#!/bin/bash

echo "=== REGISTER ==="
curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"joao","email":"joao@email.com","fullName":"Joao Silva","password":"123456"}' | jq

echo ""
echo "=== LOGIN ==="
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"joao","password":"123456"}' | jq -r '.token')
echo "Token: $TOKEN"

echo ""
echo "=== SEM TOKEN (deve retornar 401) ==="
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/rooms

echo ""
echo ""
echo "=== COM TOKEN em /api/auth/me (deve retornar 200 + username) ==="
curl -s http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN" | jq

echo ""
echo "=== LISTAR SALAS (deve retornar as 4 do seed) ==="
curl -s http://localhost:8080/api/rooms \
  -H "Authorization: Bearer $TOKEN" | jq

echo ""
echo "=== CRIAR SALA sem ser ADMIN (deve retornar 403) ==="
curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:8080/api/rooms \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Nova Sala","capacity":5}'
echo ""

echo ""
echo "=== CRIAR RESERVA (deve retornar 201 + id) ==="
BOOKING=$(curl -s -X POST http://localhost:8080/api/bookings \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"roomId":1,"startTime":"2026-04-20T14:00:00","endTime":"2026-04-20T16:00:00","purpose":"Reuniao"}')
echo "$BOOKING" | jq '{id: .id, room: .roomName, status: .status}'

echo ""
echo "=== RESERVA CONFLITANTE (deve retornar 409) ==="
curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:8080/api/bookings \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"roomId":1,"startTime":"2026-04-20T15:00:00","endTime":"2026-04-20T17:00:00","purpose":"Conflito"}'
echo ""

echo ""
echo "=== MINHAS RESERVAS (deve retornar 1 reserva) ==="
curl -s http://localhost:8080/api/bookings/me \
  -H "Authorization: Bearer $TOKEN" | jq '[.[] | {id: .id, room: .roomName, status: .status}]'

echo ""
echo "=== DISPONIBILIDADE (slot 14:00 deve estar ocupado) ==="
curl -s "http://localhost:8080/api/rooms/1/availability?date=2026-04-20" \
  -H "Authorization: Bearer $TOKEN" | jq '[.slots[] | select(.start >= "14:00" and .start < "16:00")]'
