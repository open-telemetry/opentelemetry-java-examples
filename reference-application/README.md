# OpenTelemetry Java Reference Application

This reference application demonstrates comprehensive OpenTelemetry usage in Java, following the [OpenTelemetry Getting Started Reference Application Specification](https://opentelemetry.io/docs/getting-started/reference-application-specification/).

## Features

This application showcases:

- **Traces**: Manual and automatic span creation, distributed tracing
- **Metrics**: Custom metrics, performance monitoring
- **Logs**: Structured logging with trace correlation
- **Multiple exporters**: Console, OTLP, file-based exports
- **Configuration**: Environment variables, programmatic setup, and declarative configuration
- **Docker support**: Complete setup with OpenTelemetry Collector

## Application Overview

The reference application is a dice rolling service that demonstrates OpenTelemetry capabilities using the **OpenTelemetry Java Agent** for automatic instrumentation and manual instrumentation examples:

### Endpoints

- `GET /rolldice` - Basic dice roll (returns random 1-6)
- `GET /rolldice?player=<name>` - Dice roll for a specific player
- `GET /rolldice?rolls=<n>` - Roll multiple dice
- `GET /fibonacci?n=<number>` - Calculate fibonacci (demonstrates computation tracing)
- `GET /health` - Health check endpoint
- `GET /metrics` - Prometheus metrics endpoint (when enabled)

### Scenarios Demonstrated

1. **Basic HTTP instrumentation**: Automatic span creation for HTTP requests
2. **Manual instrumentation**: Custom spans for business logic
3. **Error handling**: Error span recording and exception tracking
4. **Custom metrics**: Performance counters, histograms, gauges
5. **Baggage propagation**: Cross-cutting concerns
6. **Resource detection**: Automatic resource attribute detection

## Quick Start

### Prerequisites

- Java 17 or later
- Docker and Docker Compose (for collector setup)

### Running with Console Output

```shell
# Build the application with the Java agent
../gradlew bootJar

# Run with the Java agent for automatic instrumentation
java -javaagent:build/agent/opentelemetry-javaagent.jar -jar build/libs/app.jar
```

Then test the endpoints:
```shell
curl http://localhost:8080/rolldice
curl http://localhost:8080/rolldice?player=alice
curl http://localhost:8080/fibonacci?n=10
```

### Running with OpenTelemetry Collector

```shell
# Build the application
../gradlew bootJar

# Start the collector and application
docker-compose up --build
```

This will:
- Start the reference application on port 8080
- Start OpenTelemetry Collector on port 4317/4318
- Export telemetry data to the collector
- Output structured telemetry data to console

## Configuration

The application supports multiple configuration approaches:

### Environment Variables

```shell
export OTEL_SERVICE_NAME=dice-server
export OTEL_SERVICE_VERSION=1.0.0
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4318
export OTEL_TRACES_EXPORTER=otlp
export OTEL_METRICS_EXPORTER=otlp
export OTEL_LOGS_EXPORTER=otlp
```

### Java Agent Configuration

The application uses the OpenTelemetry Java Agent which automatically configures instrumentation based on environment variables and system properties. All standard OpenTelemetry configuration options are supported.

### Declarative Configuration

Use the included `otel-config.yaml` for file-based configuration:

```shell
export OTEL_EXPERIMENTAL_CONFIG_FILE=otel-config.yaml
```

## Understanding the Output

### Traces

The application creates spans for:
- HTTP requests (automatic)
- Business logic operations (manual)
- External calls and computations
- Error scenarios

### Metrics

The application reports:
- Request duration histograms
- Request counters by endpoint
- Error rates
- Custom business metrics (dice roll distributions)

### Logs

All logs include:
- Trace ID and Span ID for correlation
- Structured fields
- Different log levels
- Business context

## Development

### Building

```shell
../gradlew build
```

### Testing

The reference application includes comprehensive testing:

#### Unit Tests
```shell
../gradlew test
```

The test suite includes:
- **Functional tests**: Verify all endpoints return correct responses
- **Telemetry tests**: Validate OpenTelemetry data export using MockServer
  - Traces: HTTP spans, custom spans, span attributes, and events
  - Metrics: Custom counters and timers
  - Baggage: Cross-cutting concern propagation

The telemetry tests use MockServer to capture OTLP requests and verify that the application correctly generates and exports telemetry data for different scenarios.

For detailed information about telemetry testing, see [TELEMETRY-TESTING.md](TELEMETRY-TESTING.md).

### End-to-End Testing

Run the comprehensive end-to-end test that verifies the complete OpenTelemetry stack:

```shell
# Run via Gradle
../gradlew e2eTest

# Or run directly
./test-e2e.sh
```

This test:
- Builds and starts all services using `docker-compose up --build`
- Waits for services to be ready (application, collector, Prometheus)
- Tests all application endpoints
- Verifies OpenTelemetry data collection and export
- Validates Prometheus metric scraping
- Cleans up resources automatically

For detailed information about the end-to-end test, see [E2E-TEST.md](E2E-TEST.md).

### Running locally

```shell
../gradlew bootRun
```

## Docker Images

The application can be built as a Docker image:

```shell
../gradlew bootBuildImage
```

## Troubleshooting

### Common Issues

1. **No telemetry data**: Check OTEL_* environment variables
2. **Connection issues**: Verify collector endpoint configuration
3. **Missing traces**: Ensure sampling is configured correctly

### Debugging

Enable debug logging:
```shell
export OTEL_JAVAAGENT_DEBUG=true
```

Or set logging level:
```shell
export LOGGING_LEVEL_IO_OPENTELEMETRY=DEBUG
```

## Learn More

- [OpenTelemetry Java Documentation](https://opentelemetry.io/docs/languages/java/)
- [OpenTelemetry Specification](https://opentelemetry.io/docs/specs/otel/)
- [Semantic Conventions](https://opentelemetry.io/docs/specs/semconv/)