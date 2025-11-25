#!/usr/bin/env bash

set -euo pipefail

cd prometheus
../gradlew shadowJar
docker compose up -d

function ready() {
  # check that 200 is returned from the metrics endpoint
  curl -f localhost:19090/metrics > /dev/null 2>&1 || (echo "Metrics endpoint did not return 200" && exit 1)
  # check that 200 is returned from the Prometheus server
  curl -f http://localhost:9090 > /dev/null 2>&1 || (echo "Prometheus did not return 200" && exit 1)
}

# wait up to 2 minutes for the endpoints to be ready
timeout=120
interval=5
elapsed=0
while ! ready; do
  if [ $elapsed -ge $timeout ]; then
    echo "Timeout waiting for Prometheus endpoints to be ready"
    docker compose logs
    docker compose down
    exit 1
  fi
  echo "Waiting for Prometheus endpoints to be ready..."
  sleep $interval
  elapsed=$((elapsed + interval))
done

echo "Prometheus example build and endpoints are working correctly"

docker compose down
