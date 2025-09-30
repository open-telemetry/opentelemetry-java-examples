# Prometheus Example

This example demonstrates how to use the OpenTelemetry SDK to instrument a
simple application using Prometheus as the metric exporter and expose the
metrics via HTTP.

These metrics are collected by a Prometheus instance which is configured to scrape
metrics via HTTP.

# How to Run

## Prerequisites

* Java 17 or higher is required to run Gradle and build this example
* Java 8 or higher may be used to run the example once it has been built
* Docker 19.03
* Docker Compose

## 1 - Compile

```shell script
../gradlew shadowJar
```

## 2 - Run

Start the application and Prometheus via Docker Compose:

```shell
docker compose up
```

## 3 - View Metrics

To view metrics in Prometheus, navigate to:

http://localhost:9090/graph?g0.range_input=15m&g0.expr=incoming_messages&g0.tab=0

To fetch application metrics in prometheus format, run:

curl localhost:19090/metrics

To fetch application metrics in OpenMetrics format, which includes exemplars, run:

curl -H 'Accept: application/openmetrics-text; version=1.0.0; charset=utf-8' localhost:19090/metrics
