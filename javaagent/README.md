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

# How to Run

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

## Declarative Configuration

By default, this example uses the [environment variable configuration schema](https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/configuration/sdk-environment-variables.md) to configure the SDK. However, it also includes [sdk-config.yaml](./sdk-config.yaml) which demonstrates how the [declarative configuration](https://opentelemetry.io/docs/specs/otel/configuration/#declarative-configuration) scheme can be used to configure the SDK based on a YAML configuration file instead.

`sdk-config.yaml` extends the [opentelemetry-configuration sdk-config.yaml](https://github.com/open-telemetry/opentelemetry-configuration/blob/v0.3.0/examples/sdk-config.yaml) template, demonstrating:

- Configuration of instrumentation (see `.instrumentation.java`)
- Configuration of [rule-based routing sampler](https://github.com/open-telemetry/opentelemetry-java-contrib/tree/main/samplers) (see `.tracer_provider.sampler.parent_based.root`)

To use declarative configuration instead of the environment variable scheme, add the following before starting the application and Collector:

```shell
export OTEL_EXPERIMENTAL_CONFIG_FILE=/sdk-config.yaml
```

Note: toggling declarative configuration causes the env var configuration scheme to be ignored completely. However, there is support for [env var substitution](https://opentelemetry.io/docs/specs/otel/configuration/data-model/#environment-variable-substitution) within configuration files.
