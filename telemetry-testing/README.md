# Telemetry testing Example

This is an example how to test the telemetry data using [MockServer](https://www.mock-server.com/).

The example contains a simple SpringBoot application that exposes an API available at `GET http://localhost:8080/ping`

## Telemetry

When the API is called, auto instrumentation from the OpenTelemetry Java Agent records traces and
metrics.

The method `doWork()` in Controller class is annotated with the `@WithSpan` annotation which automatically generates the trace when the method is called.
Counter meter `apiCounter` increments the metric on each API call.

## Prerequisites

* Java 1.8 or higher

## Run

Go to test folder and execute the test from ApplicationTest class.

ApplicationTest contains 1 test `testTelemetry()` which verifies that:

- the traces `Controller.doWork` and `Controller.ping` were sent to the collector
- metric `apiCounter` was sent to the collector

when the API was called.

### Test set up

Gradle build script contains a task to download the OpenTelemetry Java Agent jar, which is added to the jvm arguments in the `test` task.

Additionally, the OpenTelemetry is configured with properties:

- otel.exporter.otlp.protocol=http/protobuf - set up the telemetry protocol, default is grpc
- otel.metric.export.interval=5000 - set up the interval, between the start of two export attempts, default is 60s
More information about the configuration can be found in this [OpenTelemetry doc](https://opentelemetry.io/docs/languages/java/configuration/#properties-exporters).

The MockServer library is used to mock the collector web server. It's configured with port `4318`, because for
`http/protobuf` protocol, the otel exporter endpoint is by default set to `http://localhost:4318`.

The collector mock server is configured with expectation to reply with http code 200 on every request.

To verify that the telemetry was sent to the collector we retrieve the requests from the mock server and assert the traces and metrics names.
