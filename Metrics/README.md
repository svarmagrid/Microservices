# Observability: Metrics

## Overview
Monitoring the health and performance of microservices in real time is critical. This module implements **Metrics Collection and Visualization** using Micrometer, Prometheus, and Grafana. Every service automatically exposes HTTP and JVM metrics, and a pre-built Grafana dashboard visualizes them instantly.

## What is Implemented
- **Spring Boot Actuator + Micrometer**: Added to all 4 services, exposing metrics at `/actuator/prometheus`.
- **`management.metrics.tags.service`**: Every metric is tagged with the service name for easy filtering in Grafana.
- **Custom Application Metrics**:
  - `auth-service`: `auth.login.success` and `auth.login.failure` counters
  - `post-service`: `posts.created.total` counter
- **Prometheus**: `prometheus.yml` configured to scrape `/actuator/prometheus` on all 4 services every 15 seconds.
- **Grafana Auto-Provisioning**: Datasource and dashboard are provisioned automatically on startup.
- **Pre-built Dashboard** ("Microservices Observability") with 6 panels:
  1. HTTP Request Rate (req/s) — success vs error
  2. HTTP Response Time Percentiles (p50, p95, p99)
  3. HTTP Error Rate (5xx / total)
  4. JVM Heap Memory (used vs max)
  5. JVM CPU Usage
  6. JVM Active Threads

## Services

| Service | Port | Metrics Endpoint |
|---------|------|-----------------|
| `auth-service` | 8081 | `http://localhost:8081/actuator/prometheus` |
| `post-service` | 8082 | `http://localhost:8082/actuator/prometheus` |
| `notification-service` | 8083 | `http://localhost:8083/actuator/prometheus` |
| `api-gateway` | 8080 | `http://localhost:8080/actuator/prometheus` |
| Prometheus | 9090 | http://localhost:9090 |
| Grafana | 3000 | http://localhost:3000 (admin/admin) |

## Running the Module

```bash
cd Metrics
docker-compose up --build
```

## Quick Verification

### 1. Verify a service exposes Prometheus metrics
```bash
curl http://localhost:8081/actuator/prometheus | head -30
# Expected: Lines like:
# http_server_requests_seconds_count{...} 1.0
# jvm_memory_used_bytes{...} 12345678.0
```

### 2. Check Prometheus is scraping all 4 targets
Open http://localhost:9090 → **Status → Targets**
All 4 targets should show **State: UP**.

### 3. Generate traffic to populate metrics
```bash
# Trigger login success counter
curl -X POST http://localhost:8081/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"user1","password":"password"}'

# Trigger login failure counter
curl -X POST http://localhost:8081/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"baduser","password":"wrong"}'

# Create some posts
curl -X POST http://localhost:8082/posts \
  -H 'Content-Type: application/json' \
  -d '{"title":"Test Post"}'
```

### 4. Open Grafana dashboard
1. Open http://localhost:3000 — login with **admin / admin**
2. Navigate to **Dashboards → Microservices → Microservices Observability**
3. The dashboard refreshes every 10 seconds — run more requests to see graphs update.

### 5. Query a custom metric in Prometheus
```
# In Prometheus UI (http://localhost:9090), enter:
auth_login_success_total
# Expected: counter value from auth-service
```

### 6. Query p95 response time in Prometheus
```
histogram_quantile(0.95, sum(rate(http_server_requests_seconds_bucket[1m])) by (le, service))
```
