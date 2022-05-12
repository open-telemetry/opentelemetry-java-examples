# Micrometer Shim Example

This example demonstrates a typical use case
of [micrometer1-shim](https://github.com/open-telemetry/opentelemetry-java/tree/main/micrometer1-shim).

It consists of a spring boot application with:

- A simple web API available at `GET http://localhost:8080/ping`
- Instrumented with the spring boot actuator and micrometer
- Micrometer metrics bridged to OpenTelemetry using the micrometer shim
- OpenTelemetry metrics exported with prometheus

# How to run

Run the application

```shell
../gradlew bootRun
```

Exercise the application by calling its endpoint

```shell
curl http://localhost:8080/ping
```

View micrometer metrics in prometheus format by navigating to:

http://localhost:9464
