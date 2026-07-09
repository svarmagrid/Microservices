# Observability: Logs

## Overview
In a distributed microservices environment, debugging by looking at individual service consoles is impractical. This module implements **Centralized Structured Logging** using the ELK Stack (Elasticsearch, Logstash, Kibana). All 4 services ship JSON logs over TCP to Logstash, which forwards them to Elasticsearch for querying in Kibana.

## What is Implemented
- **ELK Stack Infrastructure**: `docker-compose.yml` runs Elasticsearch (port 9200), Logstash (TCP 5000), and Kibana (port 5601).
- **`LogstashTcpSocketAppender`**: Configured in `logback-spring.xml` for all 4 services. Sends structured JSON to Logstash over TCP.
- **`logstash.conf` Pipeline**: Receives JSON on TCP port 5000 → parses it → indexes into Elasticsearch under `microservices-logs-YYYY.MM.dd`.
- **Service Name Embedded**: Each log event includes an `app_name` field (e.g., `auth-service`) for filtering in Kibana.
- **Trace IDs in Logs**: MDC fields `traceId` and `spanId` are included in every log event (prepared for correlation with distributed tracing).
- **Console + Logstash**: Both appenders active — human-readable logs in the terminal and structured JSON sent to ELK.

## Services

| Service | Port | Role |
|---------|------|------|
| `auth-service` | 8081 | Logs login events |
| `post-service` | 8082 | Logs post CRUD operations |
| `notification-service` | 8083 | Logs notification events |
| `api-gateway` | 8080 | Logs incoming requests |
| Logstash | 5000 (TCP) | Log ingestion pipeline |
| Elasticsearch | 9200 | Log storage and indexing |
| Kibana | 5601 | Log visualization and search UI |

## Running the Module

```bash
cd Logs
docker-compose up --build
```

> Wait ~60 seconds for Elasticsearch and Kibana to be fully ready before querying.

## Quick Verification

### 1. Check Kibana is ready
Open http://localhost:5601 in your browser. Wait until the home screen loads.

### 2. Generate some log events
```bash
curl http://localhost:8081/auth/health
curl http://localhost:8082/posts/health
curl http://localhost:8083/notifications/health
```

### 3. Create an index pattern in Kibana
1. Go to http://localhost:5601
2. Navigate to **Stack Management → Index Patterns**
3. Create a pattern: `microservices-logs-*`
4. Set **Time field** to `@timestamp`

### 4. View logs in Kibana Discover
Navigate to **Discover** and you should see log events with fields:
- `app_name` — the service name
- `message` — the log message
- `level` — INFO / DEBUG / WARN
- `@timestamp` — event time

### 5. Verify Logstash is receiving logs (check Logstash port)
```bash
curl http://localhost:9200/microservices-logs-*/_count
# Expected: {"count": N, ...} — N should increase as you make requests
```

### 6. Filter logs by service in Kibana
In the Discover view, add a filter: `app_name: "auth-service"` to see only auth-service logs.
