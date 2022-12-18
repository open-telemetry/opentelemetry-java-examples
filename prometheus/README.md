# Prometheus Example

This example demonstrates how to use the OpenTelemetry SDK to instrument a
simple application using Prometheus as the metric exporter and expose the
metrics via HTTP.

These are collected by a Prometheus instance which is configured to pull these
metrics via HTTP.

# How to run

## Prerequisites

* Java 1.7
* Docker 19.03
* Docker compose

## 1 - Compile

```shell script
../gradlew shadowJar
```

## 2 - Run

Start the application and prometheus via docker compose

```shell
docker-compose up
```

## 3 - View metrics

To view metrics in prometheus, navigate to:

http://localhost:9090/graph?g0.range_input=15m&g0.expr=incoming_messages&g0.tab=0

To view application metrics in prometheus format, navigate to:

http://localhost:19090/metrics

**Troubleshooting: open /etc/prometheus/prometheus.yml: permission denied**

If you see this error and localhost:9090 is not showing prometheus backend, then run: 

```
sudo chgrp -R nogroup ./prometheus.yml
```

This happens because according to the prometheus [Dockerfile](https://github.com/prometheus/prometheus/blob/main/Dockerfile), the _USER_ is set to _nobody_, which is why the `prometheus.yml` file is not accessible.
