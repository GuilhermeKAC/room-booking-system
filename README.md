# Room Booking System

API REST para gerenciamento de reservas de salas, desenvolvida com Spring Boot 3.4.4.

## Stack

- Java 17
- Spring Boot 3.4.4
- PostgreSQL 15
- Flyway (migrações de banco)
- Spring Security
- Docker / Docker Compose
- Lombok

## Configuração do ambiente

### 1. Copie o arquivo de variáveis de ambiente

```bash
cp .env.example .env
```

Edite o `.env` com os valores desejados para banco de dados e JWT.

### 2. Suba o banco de dados com Docker

```bash
docker-compose up -d postgres
```

> **Atenção:** o PostgreSQL do Docker está mapeado na porta `5433` (não a padrão `5432`) para evitar conflito com uma instalação local do PostgreSQL.

### Usando a porta padrão (5432)

Se não tiver PostgreSQL instalado localmente e quiser usar a porta padrão, edite o `docker-compose.yml`:

```yaml
ports:
  - "5432:5432"  # altere de 5433 para 5432
```

E atualize o `application.yml` ou o `.env` se necessário.

### 3. Execute a aplicação

```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

## Estrutura do projeto

```
src/
└── main/
    ├── java/com/roombooking/
    │   └── RoomBookingSystemApplication.java
    └── resources/
        ├── application.yml
        └── db/migration/
            └── V1__create_initial_schema.sql
```

## Migrações de banco

As migrações são gerenciadas pelo Flyway e ficam em `src/main/resources/db/migration/`.  
O padrão de nomenclatura é `V{numero}__{descricao}.sql`, por exemplo: `V2__create_rooms_table.sql`.