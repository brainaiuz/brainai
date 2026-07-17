.PHONY: help up down start stop restart logs ps clean build pull

.DEFAULT_GOAL := help

# Configuration - Change ENV to switch between versions
ENV ?= standard
COMPOSE_FILE := conf/docker/docker-compose.$(ENV).yml
ENV_FILE := conf/docker/.env

ifeq ($(shell command -v getenforce >/dev/null 2>&1 && getenforce || echo Disabled),Enforcing)
	export VOLUME_SUFFIX := ro,Z
else ifeq ($(shell command -v getenforce >/dev/null 2>&1 && getenforce || echo Disabled),Permissive)
	export VOLUME_SUFFIX := ro,Z
else
	export VOLUME_SUFFIX := ro
endif

export VOLUME_SUFFIX

# Service prefix based on environment
SERVICE_PREFIX := kpi

##@ General

help: ## Display this help message
	@echo "KPI Infrastructure Management [$(ENV)]"
	@echo ""
	@echo "Available Environments:"
	@echo "  local     - Full stack (MongoDB, Redis, RabbitMQ, Solr, Kafka, MinIO, PgBouncer)"
	@echo "  standard  - Default Standard stack (MongoDB, Redis, RabbitMQ, Solr, Kafka)"
	@echo "  minimal   - Minimal stack (MongoDB, Redis, RabbitMQ, Solr)"
	@echo ""
	@echo "Change environment: make ENV=local <command>"
	@echo ""
	@echo "Usage:"
	@echo "  make <target>"
	@echo ""
	@echo "Service Management (All Services):"
	@echo "  up                   Start all services"
	@echo "  down                 Stop and remove all services"
	@echo "  start                Start all stopped services"
	@echo "  stop                 Stop all running services"
	@echo "  restart              Restart all services"
	@echo ""
	@echo "Individual Service Management:"
	@echo "  up-mongodb           Start mongodb service"
	@echo "  up-redis             Start redis service"
	@echo "  up-rabbitmq          Start rabbitmq service"
	@echo "  up-kafka             Start all kafka services"
	@echo "  up-kafka-1           Start kafka-1 service"
	@echo "  up-kafka-2           Start kafka-2 service"
	@echo "  up-kafka-3           Start kafka-3 service"
	@echo "  up-solr              Start solr service"
	@echo "  up-minio             Start minio service"
	@echo "  up-pgbouncer         Start pgbouncer service (local only)"
	@echo "  up-activemq          Start activemq service (local only)"
	@echo ""
	@echo "Service Groups:"
	@echo "  up-essentials        Start essential services (MongoDB, Redis, RabbitMQ, Solr)"
	@echo "  up-databases         Start database services (MongoDB, PgBouncer)"
	@echo "  up-messaging         Start messaging services (RabbitMQ, Kafka, ActiveMQ)"
	@echo "  up-storage           Start storage services (Redis, MinIO)"
	@echo "  up-search            Start search services (Solr)"
	@echo ""
	@echo "Monitoring & Logs:"
	@echo "  logs                 Show logs for all services"
	@echo "  logs-mongodb         Show mongodb logs"
	@echo "  logs-redis           Show redis logs"
	@echo "  logs-rabbitmq        Show rabbitmq logs"
	@echo "  logs-kafka           Show all kafka logs"
	@echo "  logs-solr            Show solr logs"
	@echo "  logs-minio           Show minio logs"
	@echo "  ps                   Show status of all services"
	@echo "  ps-detailed          Show detailed status with health checks"
	@echo "  stats                Show resource usage statistics"
	@echo ""
	@echo "Maintenance:"
	@echo "  pull                 Pull latest images for all services"
	@echo "  build                Build/rebuild services"
	@echo "  clean                Remove stopped containers and volumes"
	@echo "  prune                Remove unused Docker resources"
	@echo ""
	@echo "Advanced Operations:"
	@echo "  restart-service      Restart a specific service (usage: make restart-service SERVICE=redis)"
	@echo "  exec                 Execute command in service (usage: make exec SERVICE=redis CMD='redis-cli ping')"
	@echo "  shell                Open shell in service (usage: make shell SERVICE=redis)"
	@echo ""
	@echo "Network:"
	@echo "  network-inspect      Inspect the network"
	@echo ""
	@echo "Environment Comparison:"
	@echo "  compare              Compare services across environments"

##@ Service Management (All Services)

up: ## Start Standard services
	@echo "Starting all services [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d
	@echo "All services started [$(ENV)]"

down: ## Stop and remove all services
	@echo "Stopping all services [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) down
	@echo "All services stopped [$(ENV)]"

start: ## Start all stopped services
	@echo "Starting all services [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) start
	@echo "All services started [$(ENV)]"

stop: ## Stop all running services
	@echo "Stopping all services [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) stop
	@echo "All services stopped [$(ENV)]"

restart: ## Restart all services
	@echo "Restarting all services [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) restart
	@echo "All services restarted [$(ENV)]"

##@ Individual Service Management

up-mongodb: ## Start mongodb service
	@echo "Starting mongodb [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d $(SERVICE_PREFIX)-mongodb
	@echo "mongodb started"

up-redis: ## Start redis service
	@echo "Starting redis [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d $(SERVICE_PREFIX)-redis
	@echo "redis started"

up-rabbitmq: ## Start rabbitmq service
	@echo "Starting rabbitmq [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d $(SERVICE_PREFIX)-rabbitmq
	@echo "rabbitmq started"

up-kafka: ## Start all kafka services
	@echo "Starting kafka cluster [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d $(SERVICE_PREFIX)-kafka-1 $(SERVICE_PREFIX)-kafka-2 $(SERVICE_PREFIX)-kafka-3
	@echo "kafka cluster started"

up-kafka-1: ## Start kafka-1 service
	@echo "Starting kafka-1 [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d $(SERVICE_PREFIX)-kafka-1
	@echo "kafka-1 started"

up-kafka-2: ## Start kafka-2 service
	@echo "Starting kafka-2 [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d $(SERVICE_PREFIX)-kafka-2
	@echo "kafka-2 started"

up-kafka-3: ## Start kafka-3 service
	@echo "Starting kafka-3 [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d $(SERVICE_PREFIX)-kafka-3
	@echo "kafka-3 started"

up-solr: ## Start solr service
	@echo "Starting solr [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d $(SERVICE_PREFIX)-solr
	@echo "solr started"

up-minio: ## Start minio service
	@echo "Starting minio [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d $(SERVICE_PREFIX)-minio
	@echo "minio started"

up-pgbouncer: ## Start pgbouncer service (local only)
	@echo "Starting pgbouncer [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d $(SERVICE_PREFIX)-pgbouncer
	@echo "pgbouncer started"

up-activemq: ## Start activemq service (local only)
	@echo "Starting activemq [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d $(SERVICE_PREFIX)-activemq
	@echo "activemq started"

##@ Service Groups

up-essentials: ## Start essential services (MongoDB, Redis, RabbitMQ)
	@echo "Starting essential services [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d \
		$(SERVICE_PREFIX)-mongodb \
		$(SERVICE_PREFIX)-redis \
		$(SERVICE_PREFIX)-rabbitmq \
		$(SERVICE_PREFIX)-solr
	@echo "Essential services started"

up-databases: ## Start database services
	@echo "Starting database services [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d \
		$(SERVICE_PREFIX)-mongodb \
		$(SERVICE_PREFIX)-pgbouncer
	@echo "Database services started"

up-messaging: ## Start messaging services
	@echo "Starting messaging services [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d \
		$(SERVICE_PREFIX)-rabbitmq \
		$(SERVICE_PREFIX)-kafka-1 \
		$(SERVICE_PREFIX)-kafka-2 \
		$(SERVICE_PREFIX)-kafka-3 \
		$(SERVICE_PREFIX)-activemq
	@echo "Messaging services started"

up-storage: ## Start storage services
	@echo "Starting storage services [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d \
		$(SERVICE_PREFIX)-redis \
		$(SERVICE_PREFIX)-minio
	@echo "Storage services started"

up-search: ## Start search services
	@echo "Starting search services [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) up -d \
		$(SERVICE_PREFIX)-solr
	@echo "Search services started"

##@ Monitoring & Logs

logs: ## Show logs for all services (usage: make logs SERVICE=mongodb)
	@echo "Showing logs for all services [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) logs -f

logs-mongodb: ## Show mongodb logs
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) logs -f $(SERVICE_PREFIX)-mongodb

logs-redis: ## Show redis logs
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) logs -f $(SERVICE_PREFIX)-redis

logs-rabbitmq: ## Show rabbitmq logs
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) logs -f $(SERVICE_PREFIX)-rabbitmq

logs-kafka: ## Show all kafka logs
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) logs -f $(SERVICE_PREFIX)-kafka-1 $(SERVICE_PREFIX)-kafka-2 $(SERVICE_PREFIX)-kafka-3

logs-solr: ## Show solr logs
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) logs -f $(SERVICE_PREFIX)-solr

logs-minio: ## Show minio logs
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) logs -f $(SERVICE_PREFIX)-minio

ps: ## Show status of all services
	@echo "Service Status [$(ENV)]:"
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) ps

ps-detailed: ## Show detailed status with health checks
	@echo "Detailed Service Status [$(ENV)]:"
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) ps -a --format "table {{.Name}}\t{{.Status}}\t{{.Ports}}"

stats: ## Show resource usage statistics
	@echo "Resource Usage [$(ENV)]:"
	@docker stats --no-stream $$(docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) ps -q)

##@ Maintenance

pull: ## Pull latest images for all services
	@echo "Pulling latest images [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) pull
	@echo "Images updated"

build: ## Build/rebuild services
	@echo "Building services [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) build
	@echo "Build complete"

clean: ## Remove stopped containers and volumes
	@echo "Cleaning up [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) down -v
	@echo "Cleanup complete"

prune: ## Remove unused Docker resources
	@echo "Pruning Docker resources..."
	docker system prune -f
	@echo "Prune complete"

##@ Advanced Operations

restart-service: ## Restart a specific service (usage: make restart-service SERVICE=redis)
	@echo "Restarting $(SERVICE_PREFIX)-$(SERVICE) [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) restart $(SERVICE_PREFIX)-$(SERVICE)
	@echo "$(SERVICE_PREFIX)-$(SERVICE) restarted"

exec: ## Execute command in service (usage: make exec SERVICE=redis CMD="redis-cli ping")
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) exec $(SERVICE_PREFIX)-$(SERVICE) sh -c "$(CMD)"

shell: ## Open shell in service (usage: make shell SERVICE=redis)
	@echo "Opening shell in $(SERVICE_PREFIX)-$(SERVICE) [$(ENV)]..."
	docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) exec $(SERVICE_PREFIX)-$(SERVICE) sh

##@ Network

network-inspect: ## Inspect the network
	@echo "Network Details [$(ENV)]:"
	docker network inspect $(SERVICE_PREFIX)-network

##@ Environment Comparison

compare: ## Compare services across environments
	@echo "Service Comparison:"
	@echo ""
	@echo "Minimal:"
	@docker compose -f conf/docker/docker-compose.minimal.yml config --services 2>/dev/null | sed 's/^/  - /' || echo "  Not configured"
	@echo ""
	@echo "Standard:"
	@docker compose -f conf/docker/docker-compose.standard.yml config --services 2>/dev/null | sed 's/^/  - /' || echo "  Not configured"
	@echo ""
	@echo "Local (Full):"
	@docker compose -f conf/docker/docker-compose.local.yml config --services 2>/dev/null | sed 's/^/  - /' || echo "  Not configured"