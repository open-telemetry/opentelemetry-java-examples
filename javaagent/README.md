# OpenTelemetry Java Agent Example

This example demonstrates simple usage of the OpenTelemetry Java Agent published
by [opentelemetry-java-instrumentation](https://github.com/open-telemetry/opentelemetry-java-instrumentation).

It consists of a spring boot application with:

- A gradle task for downloading the OpenTelemetry Java Agent.
- A simple web API available at `GET http://localhost:8080/ping`. When called,
  auto instrumentation from the OpenTelemetry Java Agent records spans and
  metrics. Additionally, there is manual trace and metric instrumentation, as
  well as application logging performed in the context of traces using the log4j
  API.
- A docker compose setup configured to run the application and export to
  the [collector](https://opentelemetry.io/docs/collector/) via OTLP.
- The collector is configured with
  the [OTLP receiver](https://github.com/open-telemetry/opentelemetry-collector/tree/main/receiver/otlpreceiver)
  and export it to standard out with
  the [logging exporter](https://github.com/open-telemetry/opentelemetry-collector/tree/main/exporter/loggingexporter)

## Prerequisites

* Java 1.8
* Docker compose

# How to run

Build the application jar

```shell
../gradlew bootJar
```

Run the application and the collector with docker compose

```shell
docker-compose up --build
```

In a separate shell, exercise the application by calling its endpoint

```shell
curl http://localhost:8080/ping
```

Watch for spans, metrics, and logs in the collector log output
