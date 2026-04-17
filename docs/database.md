# Banco de Dados

## Migrations (Flyway)

Os scripts ficam em `src/main/resources/db/migration/` e são executados em ordem na inicialização da aplicação. O Flyway nunca repete um script já executado.

| Arquivo | Descrição |
|---|---|
| `V1__create_initial_schema.sql` | Tabela `users` |
| `V2__create_rooms_table.sql` | Tabela `rooms` |
| `V3__create_bookings_table.sql` | Tabela `bookings` + constraint de sobreposição + trigger |
| `V4__add_views_and_seed.sql` | View `active_bookings_today` + seed de salas |

## Diagrama de entidades

```
users          rooms          bookings
─────          ─────          ────────
id (PK)        id (PK)        id (PK)
username       name           user_id (FK → users)
email          capacity       room_id (FK → rooms)
password       location       start_time
full_name      has_projector  end_time
role           has_whiteboard status
active         has_ac         purpose
created_at     status         created_at
updated_at     created_at     updated_at
               updated_at
```

## Regra de não-sobreposição

A tabela `bookings` possui a constraint `no_overlap_bookings`:

```sql
CONSTRAINT no_overlap_bookings EXCLUDE USING gist (
    room_id WITH =,
    tsrange(start_time, end_time) WITH &&
) WHERE (status = 'CONFIRMED')
```

Isso garante que o próprio banco de dados rejeite duas reservas confirmadas na mesma sala com horários sobrepostos, independente da camada de aplicação.

## View útil

```sql
-- Reservas confirmadas de hoje
SELECT * FROM active_bookings_today;
```
