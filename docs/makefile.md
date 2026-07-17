# Multi-Version Docker Compose Setup Guide

## 📦 Three Versions Available

### 1. **Minimal** (`docker-compose.minimal.yml`)
**Best for:** Quick testing, lightweight development

**Includes:**
- ✅ MongoDB
- ✅ Redis
- ✅ RabbitMQ
- ✅ Solr

**Use when:**
- Basic development and testing
- Limited system resources
- Don't need Kafka, or MinIO

---

### 2. **Standard** (`docker-compose.standard.yml`)
**Best for:** Most development scenarios

**Includes:**
- ✅ MongoDB
- ✅ Redis
- ✅ RabbitMQ
- ✅ Solr
- ✅ Kafka (3-node cluster)

**Use when:**
- Need full feature set
- Don't need PgBouncer or MinIO
- Standard development environment

---

### 3. **Local/Full** (`docker-compose.local.yml`)
**Best for:** Complete development environment

**Includes:**
- ✅ MongoDB
- ✅ Redis
- ✅ RabbitMQ
- ✅ Solr
- ✅ Kafka (3-node cluster)
- ✅ MinIO
- ✅ PgBouncer

**Use when:**
- Need everything
- Full production-like environment
- Testing all integrations

---

## 🚀 Quick Start

### Using Default (Standard)
```bash
# Start Standard services
make up

# Check status
make ps

# View logs
make logs
```

### Switching Environments
```bash
# Use minimal environment
make ENV=minimal up

# Use standard environment
make ENV=standard up

# Use local/full environment
make ENV=local up
```

### One-Command Examples
```bash
# Start specific services in minimal env
make ENV=minimal up-essentials

# Start Kafka in standard env
make ENV=standard up-kafka

# View logs in local env
make ENV=local logs-mongodb
```

---

## 📋 Service Availability Matrix

| Service   | Minimal | Standard | Local |
|-----------|---------|----------|-------|
| MongoDB   | ✅       | ✅        | ✅     |
| Redis     | ✅       | ✅        | ✅     |
| RabbitMQ  | ✅       | ✅        | ✅     |
| Solr      | ✅       | ✅        | ✅     |
| Kafka     | ❌       | ✅        | ✅     |
| MinIO     | ❌       | ✅        | ✅     |
| PgBouncer | ❌       | ❌        | ✅     |

---

## 🎯 Common Workflows

### Development Workflow 1: Start with Minimal, Add Services
```bash
# Start with basics
make ENV=minimal up

# Later need Kafka? Switch to standard
make ENV=minimal down
make ENV=standard up
```

### Development Workflow 2: Use Standard for Most Work
```bash
# Start standard environment
make ENV=standard up

# Check what's running
make ENV=standard ps

# View specific service logs
make ENV=standard logs SERVICE=kafka
```

### Development Workflow 3: Full Stack Development
```bash
# Start everything
make ENV=local up

# Monitor all services
make ENV=local ps-detailed

# View resource usage
make ENV=local stats
```

---

## 📝 Makefile Commands Reference

### Environment Selection
```bash
# Set environment variable (applies to all commands)
make ENV=minimal <command>
make ENV=standard <command>
make ENV=local <command>

# Default is 'standard' if not specified
make up  # Same as: make ENV=standard up
```

### Basic Commands
```bash
# Start all services in environment
make ENV=<env> up

# Stop all services
make ENV=<env> down

# Restart all services
make ENV=<env> restart

# View status
make ENV=<env> ps

# View detailed status
make ENV=<env> ps-detailed
```

### Individual Services
```bash
# Start specific service
make ENV=<env> up-mongodb
make ENV=<env> up-redis
make ENV=<env> up-rabbitmq
make ENV=<env> up-solr
make ENV=<env> up-kafka      # All Kafka nodes
make ENV=<env> up-kafka-1    # Single Kafka node
make ENV=<env> up-minio
make ENV=<env> up-pgbouncer  # Local only
make ENV=<env> up-activemq   # Local only
```

### Service Groups
```bash
# Essential services (MongoDB, Redis, RabbitMQ, Solr)
make ENV=<env> up-essentials

# All database services (MongoDB, PGBouncer if exist in env)
make ENV=<env> up-databases

# All messaging services (RabbitMQ, Kafka if exist in env)
make ENV=<env> up-messaging

# All storage services (Redis, MinIO if exist in env)
make ENV=<env> up-storage

# Search services (Solr)
make ENV=<env> up-search
```

### Logs
```bash
# All services
make ENV=<env> logs

# Specific service (without prefix)
make ENV=<env> logs SERVICE=redis
make ENV=<env> logs SERVICE=mongodb

# Or use dedicated commands
make ENV=<env> logs-mongodb
make ENV=<env> logs-redis
make ENV=<env> logs-rabbitmq
make ENV=<env> logs-kafka
```

### Maintenance
```bash
# Pull latest images
make ENV=<env> pull

# Clean up environment
make ENV=<env> clean

# Clean ALL environments
make clean-all

# Prune Docker resources
make prune
```

### Advanced Operations
```bash
# Restart specific service
make ENV=<env> restart-service SERVICE=redis

# Execute command in container
make ENV=<env> exec SERVICE=redis CMD="redis-cli ping"

# Open shell in container
make ENV=<env> shell SERVICE=mongodb

# Inspect network
make ENV=<env> network-inspect
```

---

## 🔄 Multi-Environment Operations

### Compare Environments
```bash
# See what services are in each environment
make compare
```

---

## 💡 Best Practices

### 1. Start Small, Scale Up
```bash
# Day 1: Start with minimal
make ENV=minimal up

# Day 5: Need Kafka, upgrade to standard
make ENV=minimal down
make ENV=standard up

# Production testing: Use full local
make ENV=local up
```

### 2. Use Service Groups
```bash
# Instead of starting services individually
make ENV=standard up-essentials  # MongoDB, Redis, RabbitMQ
make ENV=standard up-messaging   # + Kafka
```

### 3. Resource Management
```bash
# Check what's running and using resources
make ENV=standard stats

# Stop what you don't need
make ENV=standard down

# Switch to lighter environment
make ENV=minimal up
```

### 4. Debugging
```bash
# Check service status
make ENV=standard ps

# View logs for problematic service
make ENV=standard logs SERVICE=kafka

# Open shell to investigate
make ENV=standard shell SERVICE=kafka-1

# Restart if needed
make ENV=standard restart-service SERVICE=kafka-1
```

---

## 📊 Resource Usage Comparison

### Approximate Memory Usage

| Environment  | Services | RAM Usage | Disk Space |
|--------------|----------|-----------|------------|
| **Minimal**  | 4        | ~1-2 GB   | ~500 MB    |
| **Standard** | 7        | ~4-6 GB   | ~2 GB      |
| **Local**    | 9        | ~6-8 GB   | ~3 GB      |

*Note: Actual usage depends on workload and data*

---

## 🔧 Troubleshooting

### Problem: Service not available in environment
```bash
# Check which services are available
make compare

# Or try to start and see the message
make ENV=minimal up-kafka
# Output: "Kafka not available in minimal environment"
```

### Problem: Port conflicts between environments
**Solution:** Each environment uses different service names and networks:
- `kpi-minimal-*` on `kpi-minimal-network`
- `kpi-standard-*` on `kpi-standard-network`
- `kpi-local-*` on `kpi-local-network`

### Problem: Running out of resources
```bash
# Check resource usage
make stats

# Switch to lighter environment
make ENV=local down
make ENV=minimal up
```

### Problem: Need to reset everything
```bash
# Nuclear option: Clean environment
make clean

# Then start fresh
make ENV=standard up
```

---

## 🚦 Migration Between Environments

### From Minimal to Standard
```bash
# 1. Stop minimal
make ENV=minimal down

# 2. Start standard
make ENV=standard up

# Note: Data is preserved in named volumes
# kpi-minimal-mongodb-data != kpi-standard-mongodb-data
```

---

## 🎯 Quick Reference Card

```bash
# ESSENTIALS
make ENV=minimal up              # Start minimal
make ENV=standard up             # Start standard  
make ENV=local up                # Start full

# MANAGEMENT
make ENV=<env> ps                # Status
make ENV=<env> logs              # All logs
make ENV=<env> logs SERVICE=x    # Specific logs
make ENV=<env> down              # Stop all
make ENV=<env> clean             # Remove all

# COMPARISON
make compare                     # Compare environments

# TROUBLESHOOTING
make ENV=<env> stats             # Resource usage
make ENV=<env> restart-service SERVICE=x
make ENV=<env> shell SERVICE=x   # Open shell
```

Perfect for printing and keeping at your desk! 📌