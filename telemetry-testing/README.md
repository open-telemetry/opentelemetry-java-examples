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