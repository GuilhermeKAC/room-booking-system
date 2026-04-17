# Configuração do Ambiente

## Pré-requisitos

- Java 17+
- Docker e Docker Compose
- Git

> O Maven não precisa estar instalado — o projeto usa o Maven Wrapper (`./mvnw`).

## Variáveis de ambiente

Copie o arquivo de exemplo e ajuste os valores:

```bash
cp .env.example .env
```

| Variável | Descrição | Padrão |
|---|---|---|
| `DB_HOST` | Host do PostgreSQL | `localhost` |
| `DB_NAME` | Nome do banco | `room_booking_db` |
| `DB_USERNAME` | Usuário do banco | `room_user` |
| `DB_PASSWORD` | Senha do banco | — |
| `JWT_SECRET` | Chave secreta JWT (hex 64 chars) | — |

## Subindo o ambiente

```bash
# Apenas o banco (para desenvolvimento local)
docker-compose up -d postgres

# Banco + pgAdmin (interface visual)
docker-compose up -d postgres pgadmin

# Tudo (banco + api)
docker-compose up -d
```

## Acessos

| Serviço | URL | Credenciais |
|---|---|---|
| API | http://localhost:8080 | — |
| pgAdmin | http://localhost:5050 | admin@roombooking.com / admin123 |
| PostgreSQL | localhost:**5433** | conforme `.env` |

> **Atenção:** o PostgreSQL do Docker roda na porta `5433` (não a padrão `5432`) para evitar conflito com instalações locais.  
> Para usar a porta padrão, altere `"5433:5432"` para `"5432:5432"` no `docker-compose.yml`.

## Rodando a aplicação

```bash
./mvnw spring-boot:run
```

Para rodar em outro perfil:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```
