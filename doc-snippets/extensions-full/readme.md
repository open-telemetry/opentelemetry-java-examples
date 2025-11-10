# Extensions - Full Example

This is a comprehensive example that demonstrates multiple extension capabilities of the OpenTelemetry Java agent:

## Features Demonstrated

1. **MySpanProcessor** - Custom span processor that adds attributes to all spans:
   - `custom.processor: "MySpanProcessor"`
   - `custom.timestamp: <milliseconds>`

2. **MySampler** - Custom sampler implementation (samples all spans in this example)

3. **TestResourceProvider** - Adds custom resource attributes:
   - `deployment.environment: "test"`
   - `service.version: <from APP_VERSION env var>`
   - `custom.attribute: "test-value"`

4. **ConfigurableDemoSampler** - Example of a configurable sampler (via SPI)

5. **HttpHandlerInstrumentation** - Custom bytecode instrumentation for HTTP handlers

6. **TestAutoConfigurationCustomizer** - Demonstrates auto-configuration customization

## Building

To build the extension JAR:

```bash
cd /path/to/opentelemetry-java-examples
./gradlew :doc-snippets:extensions-full:shadowJar
```

The output JAR will be at: `build/libs/extensions-full-1.0-all.jar`

## Testing

This module includes OATS (OpenTelemetry Acceptance Tests) to verify key extension features work correctly.

### Prerequisites

Install OATS:
```bash
go install github.com/grafana/oats@latest
```

Build both the extension and the test application:
```bash
cd /path/to/opentelemetry-java-examples
./gradlew :doc-snippets:extensions-testapp:jar :doc-snippets:extensions-full:shadowJar
```

### Run the tests

```bash
cd doc-snippets/extensions-full
oats oats/oats.yaml
```

The tests verify:
- **Span attributes** from MySpanProcessor are added to all spans
- **Resource attributes** from TestResourceProvider are present on all traces
- Extension functionality works correctly with the OpenTelemetry Java agent

## Usage

To run an application with this extension:

```bash
java -javaagent:path/to/opentelemetry-javaagent.jar \
     -Dotel.javaagent.extensions=path/to/extensions-full-1.0-all.jar \
     -Dotel.service.name=my-service \
     -Dapp.version=1.2.3 \
     -jar your-app.jar
```

The extension will:
- Add custom attributes to all spans via MySpanProcessor
- Add resource attributes like `deployment.environment` and `service.version`
- Use the custom MySampler for sampling decisions
- Apply custom HTTP handler instrumentation