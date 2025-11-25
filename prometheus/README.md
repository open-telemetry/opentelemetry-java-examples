# Prometheus Example

This example demonstrates how to use the OpenTelemetry SDK to instrument a
simple application using Prometheus as the metric exporter and expose the
metrics via HTTP.

These metrics are collected by a Prometheus instance which is configured to scrape
metrics via HTTP.

## How to Run

### Prerequisites

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

To view metrics in Prometheus (e.g. 90th percentile), navigate to:

<http://localhost:9090/query?g0.expr=histogram_quantile%28.90%2C+sum+by%28le%29+%28rate%28super_timer_milliseconds_bucket%5B5m%5D%29%29%29&g0.show_tree=0&g0.tab=graph&g0.range_input=15m&g0.res_type=auto&g0.res_density=medium&g0.display_mode=lines&g0.show_exemplars=0>

To fetch application metrics in prometheus format, run:

```shell
curl localhost:19090/metrics
```

To fetch application metrics in OpenMetrics format, which includes exemplars, run:

```shell
curl -H 'Accept: application/openmetrics-text; version=1.0.0; charset=utf-8' localhost:19090/metrics
```
