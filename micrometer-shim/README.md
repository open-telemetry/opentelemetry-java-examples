# Micrometer Shim Example

This example demonstrates a typical use case
of [Micrometer shim](https://github.com/open-telemetry/opentelemetry-java-instrumentation/tree/main/instrumentation/micrometer/micrometer-1.5/library).

It consists of a Spring Boot application with:

- A simple web API available at `GET http://localhost:8080/ping`
- Instrumented with the Spring Boot Actuator and Micrometer
- Micrometer metrics bridged to OpenTelemetry using the Micrometer shim
- OpenTelemetry metrics exported with Prometheus

## How to Run

### Prerequisites

* Java 17 or higher is required to build and run this example

## Run

Run the application:

```shell
../gradlew bootRun
```

Exercise the application by calling its endpoint:

```shell
curl http://localhost:8080/ping
```

View micrometer metrics in prometheus format by navigating to:

<http://localhost:9464>
