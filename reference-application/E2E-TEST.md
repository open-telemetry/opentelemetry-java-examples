# End-to-End Test

This directory contains an end-to-end test (`test-e2e.sh`) that validates the complete OpenTelemetry reference application stack.

## What it tests

The test verifies:
1. **Application functionality**: All endpoints return correct responses
2. **OpenTelemetry integration**: Telemetry data is generated and exported
3. **Collector functionality**: OTLP data is received and processed
4. **Prometheus integration**: Metrics are scraped and available
5. **Docker Compose setup**: All services start and work together

## Running the test

### Prerequisites

- Docker
- Docker Compose (or `docker compose`)
- curl
- jq

### Execution

```bash
# Via Gradle
../gradlew e2eTest

# Directly
./test-e2e.sh

# Dry-run (validation only)
./test-e2e.sh --dry-run
```

## Test flow

1. **Setup**: Cleans any existing containers and builds fresh images
2. **Start services**: Runs `docker-compose up --build -d`
3. **Wait for readiness**: Polls health endpoints until services are ready
4. **Functional tests**: Tests all application endpoints
5. **Telemetry validation**: Verifies OpenTelemetry data collection
6. **Integration tests**: Checks collector and Prometheus functionality
7. **Cleanup**: Stops and removes all containers and volumes

## What's tested

### Application Endpoints
- `GET /rolldice` - Basic dice rolling
- `GET /rolldice?player=testuser` - Parameterized requests
- `GET /rolldice?rolls=3` - Multiple dice rolls
- `GET /health` - Health check

### OpenTelemetry Integration
- Java Agent instrumentation
- Custom span creation
- Metrics generation
- Log correlation
- OTLP export to collector

### Telemetry Testing
In addition to end-to-end infrastructure testing, the application includes **telemetry testing** that validates actual OpenTelemetry data export:

- **Trace validation**: Verifies spans are created with correct names, attributes, and events
- **Metric validation**: Confirms custom metrics are exported properly  
- **Baggage testing**: Validates cross-cutting concern propagation
- **MockServer integration**: Captures OTLP requests for detailed analysis

These tests run with the OpenTelemetry Java Agent and use the same protobuf parsing as the telemetry-testing example.

### Infrastructure
- OpenTelemetry Collector OTLP ingestion
- Prometheus metrics scraping
- Service networking and dependencies

## Using in CI/CD

This test can be integrated into CI/CD pipelines to ensure the reference application works correctly in a production-like environment.

Example GitHub Actions usage:
```yaml
- name: Run end-to-end test
  run: |
    cd reference-application
    ./test-e2e.sh
```

The test automatically handles cleanup and provides clear success/failure indicators.