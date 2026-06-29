#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"

echo "Starting Eureka server on port 8761..."
(cd "$ROOT" && ./mvnw -q spring-boot:run) &
EUREKA_PID=$!

sleep 15

echo "Starting Orderservice instances on ports 8081, 8082, 8083..."
(cd "$ROOT/Orderservice" && SERVER_PORT=8081 ./mvnw -q spring-boot:run) &
ORDER1_PID=$!
(cd "$ROOT/Orderservice" && SERVER_PORT=8082 ./mvnw -q spring-boot:run) &
ORDER2_PID=$!
(cd "$ROOT/Orderservice" && SERVER_PORT=8083 ./mvnw -q spring-boot:run) &
ORDER3_PID=$!

sleep 20

echo "Starting PaymentService on port 8085..."
(cd "$ROOT/PaymentService" && ./mvnw -q spring-boot:run) &
PAYMENT_PID=$!

sleep 15

echo ""
echo "All services started."
echo "  Eureka:         http://localhost:8761"
echo "  PaymentService: http://localhost:8085/pay"
echo "  Verify LB:      http://localhost:8085/pay/verify?count=9"
echo ""
echo "Press Ctrl+C to stop all services."

cleanup() {
  kill "$PAYMENT_PID" "$ORDER1_PID" "$ORDER2_PID" "$ORDER3_PID" "$EUREKA_PID" 2>/dev/null || true
}
trap cleanup EXIT INT TERM

wait
