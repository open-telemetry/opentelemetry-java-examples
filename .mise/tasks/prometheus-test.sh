#!/usr/bin/env bash
#MISE description="Run Prometheus example build"

set -euo pipefail

cd prometheus
../gradlew shadowJar
# start docker compose in background
docker compose create
docker compose start

# check that 200 is returned from the metrics endpoint
curl -f localhost:19090/metrics > /dev/null 2>&1 || (echo "Metrics endpoint did not return 200" && exit 1)
# check that 200 is returned from the Prometheus server
curl -f http://localhost:9090 > /dev/null 2>&1 || (echo "Prometheus did not return 200" && exit 1)

echo "Prometheus example build and endpoints are working correctly"

docker compose down
