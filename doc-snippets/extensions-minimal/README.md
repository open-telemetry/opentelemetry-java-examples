# Extensions - Minimal example

This code is used on the documentation site to demonstrate a "quick start" minimal example that someone can use
as a starting point when writing a new extension. The functionality is that it configures a SpanProcessor to add
a custom attribute "custom.processor:active" to the spans.

**NOTE:** This code is used for populating code examples on opentelemetry.io, so please ensure that any changes
maintain the clarity and simplicity of the example and are synced correctly with the documentation site.

## Building

To build the extension JAR:

```bash
cd /path/to/opentelemetry-java-examples
./gradlew :doc-snippets:extensions-minimal:shadowJar
```

The output JAR will be at: `build/libs/extensions-minimal-0.1.0-SNAPSHOT-all.jar`

## Testing

This module includes OATS (OpenTelemetry Acceptance Tests) to verify the extension works correctly.

### Prerequisites

Install OATS:

```bash
go install github.com/grafana/oats@latest
```

Build both the extension and the test application:

```bash
cd /path/to/opentelemetry-java-examples
./gradlew :doc-snippets:extensions-testapp:jar :doc-snippets:extensions-minimal:shadowJar
```

### Run the tests

```bash
cd doc-snippets/extensions-minimal
oats oats/oats.yaml
```

The test will verify that the custom `custom.processor: "active"` attribute is added to all spans.
