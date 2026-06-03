# Java Agent Declarative Configuration Example

This example demonstrates how to use [declarative configuration](https://opentelemetry.io/docs/specs/otel/configuration/#declarative-configuration) with the OpenTelemetry Java Agent to configure tracing behavior.

The configuration file is located at [otel-agent-config.yaml](./otel-agent-config.yaml).

This Spring Boot application includes three endpoints:

- `/actuator/health` - A health check endpoint (from Spring Boot Actuator) that is configured to be excluded from tracing
- `/api/example` - A simple API endpoint that will be traced normally
- `/api/remote` - An endpoint that makes an outgoing HTTP (client) call, used to demonstrate peer service mapping

## End-to-End Instructions

### Prerequisites

* Java 17 or higher (due to Spring Boot 3 requirement, not because of declarative config support)
* OpenTelemetry Java Agent JAR file (see next step)

Download the OpenTelemetry Java Agent:

```bash
# Download the latest OpenTelemetry Java Agent
curl -L -o opentelemetry-javaagent.jar https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar
```

### Step 1: Build the Application

```bash
# Build the JAR - Run from the javaagent-declarative-configuration directory
../gradlew bootJar
```

### Step 2: Run with OpenTelemetry Java Agent

```bash
# From the javaagent-declarative-configuration directory

# Run with the OpenTelemetry Java Agent and contrib extension
java -javaagent:opentelemetry-javaagent.jar \
     -Dotel.config.file=$(pwd)/otel-agent-config.yaml \
     -jar build/libs/javaagent-declarative-configuration.jar
```

### Step 3: Test the Endpoints

Open a new terminal and test both endpoints:

```bash
# This endpoint will NOT be traced (excluded by configuration)
curl http://localhost:8080/actuator/health

# This endpoint WILL be traced normally
curl http://localhost:8080/api/example

# This endpoint makes an outgoing client call; its client span is tagged with peer.service
curl http://localhost:8080/api/remote
```

### Step 4: Verify Tracing Behavior

Check the application logs to see:

- Health check requests (`/actuator/health`) should NOT generate traces (excluded by configuration)
- API requests (`/api/example`) should generate traces with console output

## Configuration

The `otel-agent-config.yaml` file demonstrates rule-based sampling using declarative configuration to exclude health checks from tracing:

```yaml
tracer_provider:
  sampler:
    rule_based_routing:
      fallback_sampler:
        always_on:
      span_kind: SERVER
      rules:
        - action: DROP
          attribute: url.path
          pattern: /actuator.*
```

This configuration:

- Uses the `rule_based_routing` sampler from the OpenTelemetry contrib extension
- Excludes health check endpoints (`/actuator.*`) from tracing using the `DROP` action
- Samples all other requests using the `always_on` fallback sampler
- Only applies to `SERVER` span kinds

### Peer service mapping

The `otel-agent-config.yaml` file also demonstrates peer service mapping, which maps the peer address (host name or IP)
of an outgoing client call to a logical service name:

```yaml
instrumentation/development:
  java:
    common:
      service_peer_mapping:
        - peer: localhost
          service_name: example-backend
```

This configuration:

- Replaces the `otel.instrumentation.common.peer-service-mapping` system property
- Adds a `peer.service` attribute to client spans whose peer address (`server.address`) matches a configured `peer`.
  The port is optional — omitting it matches any port.
- In this example, the `/api/remote` endpoint calls back into the application over `localhost`, so its client span is
  tagged with `peer.service=example-backend`
