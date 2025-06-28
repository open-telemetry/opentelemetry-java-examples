# Java OpenTelemetry Examples

This module contains a set of fully-functional, working examples of using the
OpenTelemetry Java APIs and SDK that should all be able to be run locally. Some
of them assume you have docker running on your local machine.

## Example modules:

- [Using the SDK AutoConfiguration module](autoconfigure)
    - This module contains a fully-functional example of using the autoconfigure
      SDK extension module to configure the SDK using only environment
      variables (or system properties).
    - Note: the `opentelemetry-sdk-extension-autoconfigure` module is still
      experimental at this time.
- [Manual instrumentation of HTTP](http)
    - This module provides an example of writing manual instrumentation for
      HTTP, both client and server.
    - Note more production-ready instrumentation for HTTP is provided as a part
      of the [OpenTelemetry Java Instrumentation](https://github.com/open-telemetry/opentelemetry-java-instrumentation)
      project.
- [Manual span creation and baggage propagation](manual-tracing)
    - This module provides an example of manually creating spans using the
      OpenTelemetry API with the ExtendedTracer.
    - Additionally, it demonstrates how to use the OpenTelemetry API to
      propagate baggage items.
- [Configuring the Jaeger Exporter](jaeger)
    - This module contains a fully-functional example of configuring the
      OpenTelemetry SDK to use a Jaeger exporter, and send some spans to it
      using the OpenTelemetry API.
    - Note: this example requires having docker installed to run the example.
- [Using the OpenTelemetry Java Agent](javaagent)
    - This module demonstrates using the OpenTelemetry Java Agent with a simple
      spring boot application. Traces, metrics, and logs are exported to a
      collector via OTLP.
- [Spring native image telemetry with OpenTelemetry Spring Starter](spring-native)
    - This module demonstrates using the OpenTelemetry Spring Boot starter with a
      Graal VM native image. Traces and metrics are exported to a collector via OTLP.
- [Configuring Log Appenders](log-appender)
    - This module demonstrates how to configure the Log4j and Logback appenders to
      bridge logs into the OpenTelemetry Log SDK.
- [Configuring the Logging Exporters](logging)
    - This module contains a fully-functional example of configuring the
      OpenTelemetry SDK to use a logging exporter.
- [Exporting Application logs using JSON logging in Kubernetes](logging-k8s-stdout-otlp-json)
    - This module demonstrates how to export application logs using JSON logging
      in Kubernetes.
- [Using the OpenTelemetry metrics API](metrics)
    - This module contains examples of using the OpenTelemetry metrics APIs.
- [Using OpenTelemetry Micrometer shim](micrometer-shim)
    - This module contains an example of a typical micrometer setup (spring boot
      with spring boot actuator) configured to bridge metrics to OpenTelemetry
      with the micrometer shim.
    - Note: the micrometer shim is still experimental at this time.
- [Setting up OTLP exporters](otlp)
    - OTLP is the OpenTelemetry Protocol. This module will demonstrate how to
      configure the OTLP exporters, and send data to the OpenTelemetry collector
      using them.
    - Note: this example requires having docker installed to run the example.
- [Setting up the Prometheus exporter](prometheus)
    - The module shows how to configure the OpenTelemetry SDK to expose an
      endpoint that can be scraped by Prometheus.
    - Note: the prometheus metric reader is still experimental at this time.
- [Manually Configuring the SDK](sdk-usage)
    - This module shows some concrete examples of manually configuring the Java
      OpenTelemetry SDK for Tracing.
- [Telemetry Testing](telemetry-testing)
    - This module demonstrates how to test OpenTelemetry instrumentation with a
      MockServer.
- [Setting up the Zipkin exporter](zipkin)
    - This module contains a fully-functional example of configuring the
      OpenTelemetry SDK to use a Zipkin exporter, and send some spans to a
      zipkin backend using the OpenTelemetry API.
    - Note: this example requires having docker installed to run the example.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

### Maintainers

- [OpenTelemetry Java Maintainers](https://github.com/open-telemetry/opentelemetry-java#maintainers)
- [OpenTelemetry Java Instrumentation Maintainers](https://github.com/open-telemetry/opentelemetry-java-instrumentation#maintainers)
- [OpenTelemetry Java Contrib Maintainers](https://github.com/open-telemetry/opentelemetry-java-contrib#maintainers)

### Approvers

- [OpenTelemetry Java Approvers](https://github.com/open-telemetry/opentelemetry-java#approvers)
- [OpenTelemetry Java Instrumentation Approvers](https://github.com/open-telemetry/opentelemetry-java-instrumentation#approvers)
- [OpenTelemetry Java Contrib Approvers](https://github.com/open-telemetry/opentelemetry-java-contrib#approvers)
