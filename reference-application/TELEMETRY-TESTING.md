# Telemetry Testing

This document explains the telemetry testing approach used in the reference application to validate OpenTelemetry data export.

## Overview

The `TelemetryTest` class demonstrates how to test that your application correctly generates and exports OpenTelemetry telemetry data. This approach is based on the [telemetry-testing example](../telemetry-testing) in the repository.

## How it Works

### MockServer Setup
- Uses [MockServer](https://www.mock-server.com/) to simulate an OTLP collector
- Listens on port 4318 (default OTLP HTTP endpoint)
- Captures all OTLP requests sent by the OpenTelemetry Java Agent

### Test Configuration
The test suite is configured to:
- Run with OpenTelemetry Java Agent attached (`-javaagent`)
- Export telemetry using `http/protobuf` protocol
- Set faster metric export interval (5 seconds vs default 60 seconds)
- Suppress MockServer logging to avoid circular telemetry

### Protobuf Parsing
Uses OpenTelemetry protocol buffers to parse captured requests:
- `ExportTraceServiceRequest` for traces/spans
- `ExportMetricsServiceRequest` for metrics
- Direct access to span attributes, events, and metric values

## What Gets Tested

### Traces
- **HTTP spans**: Automatic instrumentation spans (e.g., `GET /rolldice`)
- **Custom spans**: Manual spans created in application code (e.g., `roll-dice`, `fibonacci-calculation`)
- **Span hierarchy**: Parent-child relationships between spans
- **Attributes**: Custom attributes like `dice.player`, `fibonacci.n`
- **Events**: Custom events like `dice-rolled`, `fibonacci-calculated`
- **Error handling**: Exception recording and error status

### Metrics
- **Custom counters**: `dice_rolls_total`, `fibonacci_calculations_total`
- **Custom timers**: `dice_roll_duration_seconds`, `fibonacci_duration_seconds`
- **Micrometer integration**: Metrics created via Micrometer and exported via OpenTelemetry

### Baggage
- **Cross-cutting data**: Player names, request types
- **Propagation**: Baggage values accessible across span boundaries

## Test Scenarios

### Basic Functionality
```java
@Test
public void testDiceRollTelemetry() {
    // Call endpoint
    template.getForEntity("/rolldice", String.class);
    
    // Verify spans are created
    await().untilAsserted(() -> {
        var spans = extractSpansFromRequests(requests);
        assertThat(spans)
            .extracting(Span::getName)
            .contains("GET /rolldice", "roll-dice", "roll-single-die");
    });
}
```

### Complex Scenarios
- **Multiple operations**: Testing endpoints that create multiple spans
- **Parameterized requests**: Verifying span attributes contain request parameters
- **Error conditions**: Testing that exceptions are properly recorded
- **Performance scenarios**: Large computations that generate multiple events

## Implementation Details

### Java Agent Configuration
```kotlin
jvmArgs = listOf(
    "-javaagent:${agentJarPath}",
    "-Dotel.metric.export.interval=5000",
    "-Dotel.exporter.otlp.protocol=http/protobuf",
    "-Dmockserver.logLevel=off"
)
```

### Request Parsing
```java
private List<Span> extractSpansFromRequests(HttpRequest[] requests) {
    return Arrays.stream(requests)
        .map(HttpRequest::getBody)
        .flatMap(body -> getExportTraceServiceRequest(body).stream())
        .flatMap(r -> r.getResourceSpansList().stream())
        .flatMap(r -> r.getScopeSpansList().stream())
        .flatMap(r -> r.getSpansList().stream())
        .collect(Collectors.toList());
}
```

## Benefits

### Comprehensive Validation
- **End-to-end verification**: Tests actual OTLP export, not just in-memory data
- **Protocol-level testing**: Uses the same protobuf format as real collectors
- **Realistic conditions**: Tests with the actual Java Agent configuration

### Development Confidence
- **Regression testing**: Catch telemetry regressions before production
- **Documentation**: Tests serve as living examples of expected telemetry
- **Debugging**: Easy to inspect actual telemetry data during development

## Running the Tests

```bash
# Run all tests (including telemetry tests)
../gradlew test

# Run only telemetry tests
../gradlew test --tests "TelemetryTest"

# Run specific test method
../gradlew test --tests "TelemetryTest.testDiceRollTelemetry"
```

## Troubleshooting

### Common Issues

1. **Port conflicts**: Ensure port 4318 is available
2. **Timing issues**: Use appropriate `await()` timeouts for telemetry export
3. **Agent not loaded**: Verify Java Agent is properly attached in test configuration
4. **Network issues**: MockServer requires localhost connectivity

### Debugging Tips

- Enable debug logging: `-Dotel.javaagent.debug=true`
- Inspect raw requests: Log `request.getBodyAsString()` before parsing
- Check span timing: Some spans may export in separate batches
- Verify test isolation: Each test should reset MockServer expectations

## Further Reading

- [OpenTelemetry Java Agent Configuration](https://opentelemetry.io/docs/languages/java/configuration/)
- [MockServer Documentation](https://www.mock-server.com/)
- [OpenTelemetry Protocol Specification](https://opentelemetry.io/docs/specs/otlp/)
- [Telemetry Testing Example](../telemetry-testing/README.md)