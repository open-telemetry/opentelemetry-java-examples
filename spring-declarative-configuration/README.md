# Spring Boot Declarative Configuration Example

This example demonstrates how to use [declarative configuration](https://opentelemetry.io/docs/specs/otel/configuration/#declarative-configuration) with the OpenTelemetry Spring Boot Starter (requires version 2.22.0+). Declarative configuration allows you to control tracing and metrics behavior using a YAML file, without code changes.

The configuration file is located at [`application.yaml`](./application.yaml).

This Spring Boot application includes two endpoints:
- `/actuator/health` - A health check endpoint (from Spring Boot Actuator) that is configured to be excluded from tracing
- `/api/example` - A simple API endpoint that will be traced normally

## End-to-End Instructions

### Prerequisites
* Java 17 or higher
* OpenTelemetry Spring Boot Starter version 2.22.0 or newer

Add the starter to your `build.gradle`:
```kotlin
dependencies {
  implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter:2.22.0")
}
```

### Step 1: Build the Application

```bash
# Build the JAR - Run from the spring-declarative-configuration directory
../gradlew bootJar
```

### Step 2: Run the Application

```bash
# From the spring-declarative-configuration directory
java -jar build/libs/spring-declarative-configuration.jar
```

### Step 3: Test the Endpoints

Open a new terminal and test both endpoints:

```bash
# This endpoint will NOT be traced (excluded by configuration)
curl http://localhost:8080/actuator/health

# This endpoint WILL be traced normally
curl http://localhost:8080/api/example
```

### Step 4: Verify Tracing Behavior

Check your tracing backend or logs to see:
- Health check requests (`/actuator/health`) should NOT generate traces (excluded by configuration)
- API requests (`/api/example`) should generate traces

## Declarative Configuration

Declarative configuration is enabled by setting the `file_format` property in your `application.yaml` under the `otel:` root node. All configuration must be indented under `otel:` for Spring Boot.

See [OpenTelemetry Configuration Schema and Examples](https://github.com/open-telemetry/opentelemetry-configuration) for details.

Example configuration to exclude health checks from tracing:

```yaml
otel:
  file_format: declarative
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

**Important Notes:**
- Spring Boot uses `:` as the separator for default values in property expressions, e.g. `${OTEL_EXPORTER_OTLP_ENDPOINT:http://localhost:4318}/v1/traces` (not `:-`).
- The auto-suggested properties in IDEs may be incorrect, as they are based on the non-declarative config schema.
- The `file_format` property is required to opt-in to declarative configuration.
- Refer to the [`application.yaml`](./application.yaml) configuration file in this module for the complete list of available properties and their descriptions.

## Programmatic Configuration Example

If you need to filter out health checks programmatically (instead of declarative config), see [`OpenTelemetryConfig.java`](../spring-native/src/main/java/io/opentelemetry/example/graal/OpenTelemetryConfig.java) in the `spring-native` module. The `configureSampler` method demonstrates how to drop actuator endpoints from tracing using the RuleBasedRoutingSampler:

```java
// ...existing code...
private RuleBasedRoutingSampler configureSampler(Sampler fallback, ConfigProperties config) {
  return RuleBasedRoutingSampler.builder(SpanKind.SERVER, fallback)
      .drop(UrlAttributes.URL_PATH, "^/actuator")
      .build();
}
// ...existing code...
```

## Further Reading
- [OpenTelemetry Declarative Configuration Schema](https://github.com/open-telemetry/opentelemetry-configuration)
- [Spring Boot Starter Documentation](https://github.com/open-telemetry/opentelemetry-java-instrumentation/tree/main/instrumentation/spring/spring-boot-autoconfigure)
