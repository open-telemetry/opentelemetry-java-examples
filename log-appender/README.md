# OpenTelemetry Log Appender Example

This example demonstrates an application configured to use the OpenTelemetry log
appenders to bridge logs into the OpenTelemetry Log SDK and export
via [OTLP](https://opentelemetry.io/docs/reference/specification/protocol/otlp/).

Details about the example:

* The OpenTelemetry Log SDK is configured to export data to
  the [Collector](https://opentelemetry.io/docs/collector/), which prints the
  logs to the console
* The application is configured with a variety of log solutions:
  * Log4j API [configured](./src/main/resources/log4j2.xml) to print logs to the
    console and
    the [OpenTelemetry Log4j Appender](https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/main/instrumentation/log4j/log4j-appender-2.17/library/README.md)
  * SLF4J API [configured with Logback](./src/main/resources/logback.xml) to
    print logs to the console and
    the [OpenTelemetry Logback Appender](https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/main/instrumentation/logback/logback-appender-1.0/library/README.md)
  * [JUL to SLF4J](./build.gradle.kts), which bridges JUL logs to the SLF4J API and
    ultimately to Logback
* Demonstrates how trace context is propagated to logs when recorded within a
  span

## Prerequisites

* Java 17 or higher is required to run Gradle and build this example
* Java 11 or higher is required to run the compiled example
* Docker Compose

# How to Run

Run the Collector via Docker:

```shell
docker compose up
```

In a separate shell, run the application:

```shell
../gradlew run
```

Watch the Collector logs to see exported log records.
