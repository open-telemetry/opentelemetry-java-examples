# OpenTelemetry Java Agent Example

This example demonstrates simple usage of the OpenTelemetry Java Agent published
by [opentelemetry-java-instrumentation](https://github.com/open-telemetry/opentelemetry-java-instrumentation).

It consists of a Spring Boot application with:

- A Gradle task for downloading the OpenTelemetry Java Agent
- A simple web API available at `GET http://localhost:8080/ping`. When called,
  auto-instrumentation from the OpenTelemetry Java Agent records spans and
  metrics. Additionally, there is manual trace and metric instrumentation, as
  well as application logging performed in the context of traces using the Log4j
  API
- A Docker Compose setup configured to run the application and export to
  the [Collector](https://opentelemetry.io/docs/collector/) via OTLP
- The Collector is configured with
  the [OTLP receiver](https://github.com/open-telemetry/opentelemetry-collector/tree/main/receiver/otlpreceiver)
  and exports to standard out with
  the [logging exporter](https://github.com/open-telemetry/opentelemetry-collector/tree/main/exporter/debugexporter)

## Prerequisites

* Java 17 or higher is required to build and run this example
* Docker Compose

## How to Run

Build the application jar:

```shell
../gradlew bootJar
```

Run the application and the Collector with Docker Compose:

```shell
docker compose up --build
```

In a separate shell, exercise the application by calling its endpoint:

```shell
curl http://localhost:8080/ping
```

Watch for spans, metrics, and logs in the Collector log output.
