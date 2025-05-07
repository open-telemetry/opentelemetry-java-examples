# OpenTelemetry Log Appender built-in OpenTelemetry Java Agent Example

This example demonstrates an application configured to use the OpenTelemetry Log
Appenders built-in OpenTelemetry Java Agent to bridge logs into the OpenTelemetry Log SDK, and export
via [OTLP](https://opentelemetry.io/docs/reference/specification/protocol/otlp/).

Details about the example:

* The OpenTelemetry Log SDK is configured to export data to
  the [collector](https://opentelemetry.io/docs/collector/), which prints the
  logs to the console.
* The application is configured with a variety of log solutions:
  * Log4j API [configured](./src/main/resources/log4j2.xml) to print logs to the
    console and
    the OpenTelemetry Java Agent brings built-in
    [OpenTelemetry Log4J Appender](https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/main/instrumentation/log4j/log4j-appender-2.17/javaagent/README.md).
  * SLF4J API [configured with Logback](./src/main/resources/logback.xml) to
    print logs to the console and
    the OpenTelemetry Java Agent brings built-in
    [OpenTelemetry Logback Appender](https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/main/instrumentation/logback/logback-appender-1.0/javaagent/README.md).
  * [JUL to SLF4J](./build.gradle.kts), which bridges JUL logs to the SLF4J API, and
    ultimately to Logback.
* Demonstrates how trace context is propagated to logs when recorded within a
  span.

## Prerequisites

* Java 1.8
* Docker compose

# How to run

Run the collector via docker

```shell
docker-compose up
```

In a separate shell, run the application

```shell
export \
OTEL_SERVICE_NAME=log4j-example \
OTEL_METRICS_EXPORTER=none \
OTEL_TRACES_EXPORTER=none \
OTEL_LOGS_EXPORTER=otlp

../gradlew run
```

Watch the collector logs to see exported log records

NOTE: The OpenTelemetry Java Agent uses `http/protobuf` by default, optionally switch to use `grpc` protocol
```shell
export OTEL_EXPORTER_OTLP_PROTOCOL=grpc
```
