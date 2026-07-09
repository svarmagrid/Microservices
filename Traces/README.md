# Observability: Traces

## Overview
When a single user request spans multiple microservices, it is extremely difficult to identify where bottlenecks or errors occur. This module implements **Distributed Tracing** using Micrometer Tracing and Zipkin to visualize the complete end-to-end journey of a request as a waterfall of spans.

## What is Implemented
- **Micrometer Tracing + Brave**: All 4 services automatically generate and propagate `traceId` and `spanId`.
- **100% Sampling Rate**: `management.tracing.sampling.probability: 1.0` — every single request is traced (ideal for development).
- **Zipkin Reporter**: Completed spans are sent to Zipkin asynchronously.
- **Cross-Service Trace Propagation**: B3 headers (`X-B3-TraceId`, `X-B3-SpanId`, `X-B3-ParentSpanId`) are injected into outgoing HTTP calls automatically.
- **`AuthClient` Feign Interface** (in `post-service`): Calls `auth-service` for token validation. Micrometer auto-injects B3 headers into the Feign request, linking both spans under the same `traceId` in Zipkin.
- **Trace IDs in Logs**: Log pattern includes `[appName, traceId, spanId]` — useful for correlating logs with traces.

## Key Demo: Cross-Service Trace
`POST /posts` on `post-service` → calls `auth-service` via Feign to validate the JWT → Zipkin shows **both spans linked** under the same `traceId`:
```
Trace ID: abc123
├── post-service: POST /posts           [0ms → 45ms]
│   └── auth-service: GET /auth/validate   [5ms → 30ms]
```

## Services

| Service | Port | Role |
|---------|------|------|
| `auth-service` | 8081 | Validates tokens; receives cross-service trace |
| `post-service` | 8082 | Creates posts; calls auth-service via Feign |
| `notification-service` | 8083 | Handles notification events |
| `api-gateway` | 8080 | Routes requests, propagates trace headers |
| Zipkin | 9411 | http://localhost:9411 — trace visualization |

## Running the Module

```bash
cd Traces
docker-compose up --build
```

## Quick Verification

### 1. Open Zipkin UI
Open http://localhost:9411 in your browser.

### 2. Trigger a single-service trace (GET — only one span)
```bash
curl http://localhost:8082/posts
# One span: post-service only
```

### 3. Trigger a cross-service trace (POST — two linked spans)
```bash
curl -X POST http://localhost:8082/posts \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer mytoken' \
  -d '{"title":"Traced Post","content":"Hello"}'
# Creates 2 spans:
#   1. post-service: POST /posts
#   2. auth-service: GET /auth/validate  (child span, same traceId)
```

### 4. View the trace in Zipkin
1. Open http://localhost:9411
2. Click **Find Traces**
3. You should see a trace with **2 spans** — `post-service` and `auth-service`
4. Click the trace to see the **waterfall diagram** showing exact timing of each span

### 5. Verify trace ID appears in service logs
```bash
docker logs post-service 2>&1 | grep "traceId"
# Expected log lines like:
# INFO [post-service,64b7f3a1c2d4e8f0,8a1b2c3d4e5f6a7b] ... Creating post
```

### 6. Generate multiple traces and search by service
In Zipkin UI:
- Set **Service**: `post-service`
- Set **Span Name**: `http post /posts`
- Click **Find Traces** to see all captured traces
