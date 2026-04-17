# Arquitetura

## Stack

| Camada | Tecnologia |
|---|---|
| API REST | Spring Boot 3.4.4 + Spring MVC |
| Segurança | Spring Security 6 + JWT (jjwt 0.12.6) |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | PostgreSQL 15 |
| Migrations | Flyway |
| Build | Maven (via Maven Wrapper) |
| Runtime | Docker + Docker Compose |

## Estrutura de pacotes

```
com.roombooking/
├── config/
│   ├── ApplicationConfig.java   # PasswordEncoder, AuthenticationProvider, AuthenticationManager
│   └── SecurityConfig.java      # Filtros, rotas públicas, CSRF, sessão stateless
├── controller/
│   └── AuthController.java      # POST /api/auth/register, POST /api/auth/login, GET /api/auth/me
├── service/
│   └── UserService.java         # Registro de usuário, implementa UserDetailsService
├── repository/
│   ├── UserRepository.java
│   ├── RoomRepository.java
│   └── BookingRepository.java
├── model/
│   ├── User.java
│   ├── Room.java
│   ├── Booking.java
│   └── enums/
│       ├── UserRole.java        # ADMIN, USER
│       ├── RoomStatus.java      # AVAILABLE, MAINTENANCE
│       └── BookingStatus.java   # CONFIRMED, CANCELLED, COMPLETED
├── dto/
│   ├── request/
│   │   ├── RegisterRequest.java
│   │   └── LoginRequest.java
│   └── response/
│       └── JwtResponse.java
├── security/
│   ├── JwtService.java              # Geração e validação de tokens
│   └── JwtAuthenticationFilter.java # Intercepta requisições e valida JWT
└── exception/
    ├── GlobalExceptionHandler.java
    ├── ResourceNotFoundException.java
    └── ConflictException.java
```

## Fluxo de uma requisição

```
Cliente
  → JwtAuthenticationFilter (valida token)
  → Controller (recebe e valida o DTO)
  → Service (aplica regras de negócio)
  → Repository (acessa o banco)
  → PostgreSQL
```

## Perfis de ambiente

| Perfil | Ativação | Uso |
|---|---|---|
| `dev` | padrão | Logs detalhados, SQL visível no console |
| `prod` | `--spring.profiles.active=prod` | Logs limpos |
