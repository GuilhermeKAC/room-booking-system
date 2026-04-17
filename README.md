# Room Booking System

API REST para reserva de salas corporativas, desenvolvida com Java 17 e Spring Boot 3.4.4.  
Autenticação JWT, controle de acesso por perfil (ADMIN/USER), verificação de conflito de horários e documentação automática via Swagger.

---

## Funcionalidades

- Cadastro e autenticação de usuários com JWT
- CRUD de salas (criação e edição restritos a ADMIN)
- Criação de reservas com verificação de conflito de horários
- Cancelamento de reservas (mínimo 2h de antecedência)
- Consulta de disponibilidade por sala e data (slots de 30 minutos)
- Documentação interativa via Swagger UI

---

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.4.4 |
| Segurança | Spring Security 6 + JWT (jjwt 0.12.6) |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | PostgreSQL 15 |
| Migrations | Flyway |
| Documentação | SpringDoc OpenAPI (Swagger) |
| Build | Maven (Maven Wrapper) |
| Infra | Docker + Docker Compose |

---

## Pré-requisitos

- Docker e Docker Compose
- Java 17+
- Maven (ou use o `./mvnw` incluído no projeto)

---

## Como rodar

### 1. Clone o repositório

```bash
git clone https://github.com/GuilhermeKAC/room-booking-system.git
cd room-booking-system
```

### 2. Configure as variáveis de ambiente

```bash
cp .env.example .env
```

Edite o `.env` com os valores desejados. Os defaults já funcionam para desenvolvimento local.

### 3. Suba o banco de dados

```bash
docker compose up -d postgres
```

> O PostgreSQL do Docker está mapeado na porta **5433** para evitar conflito com instalações locais do PostgreSQL na porta padrão 5432.

### 4. Exporte as variáveis e rode a aplicação

```bash
export $(grep -v '^#' .env | xargs) && ./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

---

## Documentação da API

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Principais endpoints

### Autenticação

| Método | Endpoint | Acesso | Descrição |
|---|---|---|---|
| POST | `/api/auth/register` | Público | Registra novo usuário |
| POST | `/api/auth/login` | Público | Retorna token JWT |
| GET | `/api/auth/me` | Autenticado | Dados do usuário logado |

### Salas

| Método | Endpoint | Acesso | Descrição |
|---|---|---|---|
| GET | `/api/rooms` | Autenticado | Lista todas as salas |
| GET | `/api/rooms/{id}` | Autenticado | Detalhe de uma sala |
| POST | `/api/rooms` | ADMIN | Cria sala |
| PUT | `/api/rooms/{id}` | ADMIN | Atualiza sala |
| DELETE | `/api/rooms/{id}` | ADMIN | Remove sala |
| GET | `/api/rooms/{id}/availability?date=YYYY-MM-DD` | Autenticado | Slots disponíveis |

### Reservas

| Método | Endpoint | Acesso | Descrição |
|---|---|---|---|
| POST | `/api/bookings` | Autenticado | Cria reserva |
| GET | `/api/bookings/me` | Autenticado | Minhas reservas |
| GET | `/api/bookings/room/{roomId}` | Autenticado | Reservas de uma sala |
| DELETE | `/api/bookings/{id}` | Autenticado | Cancela reserva |

---

## Autenticação

Todos os endpoints protegidos exigem o token JWT no header:

```
Authorization: Bearer <token>
```

---

## Estrutura do projeto

```
src/
├── main/
│   ├── java/com/roombooking/
│   │   ├── config/          # SecurityConfig, ApplicationConfig
│   │   ├── controller/      # AuthController, RoomController, BookingController
│   │   ├── dto/             # request/ e response/ (Java records)
│   │   ├── exception/       # GlobalExceptionHandler, exceções customizadas
│   │   ├── model/           # User, Room, Booking + enums
│   │   ├── repository/      # JpaRepository + queries customizadas
│   │   ├── security/        # JwtService, JwtAuthenticationFilter
│   │   └── service/         # UserService, RoomService, BookingService, AvailabilityService
│   └── resources/
│       ├── application.yml
│       ├── application-dev.yml
│       ├── application-prod.yml
│       └── db/migration/    # V1 a V4 — schema, índices, view, seed
└── test/
    └── java/com/roombooking/
        ├── controller/      # AuthControllerTest (integração com MockMvc)
        └── service/         # BookingServiceTest (unitário com Mockito)
```

---

## Testes

```bash
# Criar banco de teste (apenas na primeira vez)
docker exec -it room-booking-db psql -U room_user -d room_booking_db -c "CREATE DATABASE room_booking_db_test;"

# Rodar os testes
export $(grep -v '^#' .env | xargs) && ./mvnw test -q
```

---

## Variáveis de ambiente

| Variável | Descrição | Default |
|---|---|---|
| `DB_HOST` | Host do banco | `localhost` |
| `DB_PORT` | Porta do banco | `5433` |
| `DB_NAME` | Nome do banco | `room_booking_db` |
| `DB_USERNAME` | Usuário do banco | `room_user` |
| `DB_PASSWORD` | Senha do banco | — |
| `JWT_SECRET` | Chave secreta JWT (hex 64 chars) | — |
