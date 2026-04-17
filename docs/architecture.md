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
├── config/         # SecurityConfig, WebConfig
├── controller/     # Endpoints REST
├── service/        # Lógica de negócio
├── repository/     # Acesso ao banco (Spring Data)
├── model/
│   ├── entity/     # Entidades JPA
│   └── enums/      # UserRole, RoomStatus, BookingStatus
├── dto/
│   ├── request/    # Objetos de entrada da API
│   └── response/   # Objetos de saída da API
├── security/       # JwtService, JwtAuthenticationFilter
├── exception/      # GlobalExceptionHandler, exceções customizadas
└── util/           # DateTimeUtil
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
