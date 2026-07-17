# Docker Compose Quick Reference Card

If you need manual refer to: [Docker Manual Guide](docker-manual.md)

One-page reference for direct Docker Compose usage (no Make required).

---

## 📁 Files

```
conf/docker/
├── docker-compose.minimal.yml    → Minimal (MongoDB, Redis, RabbitMQ, Solr)
├── docker-compose.standard.yml   → Standard, recommended* (+ Kafka)
├── docker-compose.local.yml      → Full (+ PgBouncer, MinIO)
└── .env
```

---

## 🚀 Essential Commands

### Start All Services

```bash
# Minimal
docker compose -f conf/docker/docker-compose.minimal.yml up -d

# Standard
docker compose -f conf/docker/docker-compose.standard.yml up -d

# Local
docker compose -f conf/docker/docker-compose.local.yml up -d
```

### Stop All Services

```bash
# Minimal
docker compose -f conf/docker/docker-compose.minimal.yml down

# Standard
docker compose -f conf/docker/docker-compose.standard.yml down

# Local
docker compose -f conf/docker/docker-compose.local.yml down
```

### View Status

```bash
# Minimal
docker compose -f conf/docker/docker-compose.minimal.yml ps

# Standard
docker compose -f conf/docker/docker-compose.standard.yml ps

# Local
docker compose -f conf/docker/docker-compose.local.yml ps
```

### View Logs

```bash
# All services (follow)
docker compose -f conf/docker/docker-compose.standard.yml logs -f

# Specific service
docker compose -f conf/docker/docker-compose.standard.yml logs -f kpi-standard-mongodb
```

---

## 🎯 Individual Services

### Service Names by Environment

| Service    | Minimal                | Standard                | Local                 |
|------------|------------------------|-------------------------|-----------------------|
| MongoDB    | `kpi-minimal-mongodb`  | `kpi-standard-mongodb`  | `kpi-local-mongodb`   |
| Redis      | `kpi-minimal-redis`    | `kpi-standard-redis`    | `kpi-local-redis`     |
| RabbitMQ   | `kpi-minimal-rabbitmq` | `kpi-standard-rabbitmq` | `kpi-local-rabbitmq`  |
| Solr       | `kpi-standard-solr`    | `kpi-standard-solr`     | `kpi-local-solr`      |
| Kafka-1    | ❌                      | `kpi-standard-kafka-1`  | `kpi-local-kafka-1`   |
| Kafka-2    | ❌                      | `kpi-standard-kafka-2`  | `kpi-local-kafka-2`   |
| Kafka-3    | ❌                      | `kpi-standard-kafka-3`  | `kpi-local-kafka-3`   |
| MinIO      | ❌                      | ❌                       | `kpi-local-minio`     |
| PgBouncer  | ❌                      | ❌                       | `kpi-local-pgbouncer` |

### Start Specific Service (Standard Example)

```bash
# MongoDB only
docker compose -f conf/docker/docker-compose.standard.yml up -d kpi-standard-mongodb

# Redis only
docker compose -f conf/docker/docker-compose.standard.yml up -d kpi-standard-redis

# Kafka cluster (all 3)
docker compose -f conf/docker/docker-compose.standard.yml up -d kpi-standard-kafka-1 kpi-standard-kafka-2 kpi-standard-kafka-3

# Multiple services
docker compose -f conf/docker/docker-compose.standard.yml up -d kpi-standard-mongodb kpi-standard-redis kpi-standard-rabbitmq
```

---

## 🔧 Common Operations

### Restart Service

```bash
docker compose -f conf/docker/docker-compose.standard.yml restart kpi-standard-redis
```

### Stop Service

```bash
docker compose -f conf/docker/docker-compose.standard.yml stop kpi-standard-mongodb
```

### Execute Command

```bash
# MongoDB shell
docker compose -f conf/docker/docker-compose.standard.yml exec kpi-standard-mongodb mongosh

# Redis CLI
docker compose -f conf/docker/docker-compose.standard.yml exec kpi-standard-redis redis-cli
```

### Open Shell

```bash
docker compose -f conf/docker/docker-compose.standard.yml exec kpi-standard-mongodb sh
```

### Pull Latest Images

```bash
docker compose -f conf/docker/docker-compose.standard.yml pull
```

### Remove Everything (Including Volumes)

```bash
docker compose -f conf/docker/docker-compose.standard.yml down -v
```

---

## 📱 Print This!

Keep this card handy for quick reference without needing Make or helper scripts.

P.S. If you need any help on setup, feel free to reach https://t.me/aknbdev