Instructions for Copilot: 
- Take ../javaagent-declarative-configuration as a basis: replace this file with the readme content and adjust as needed.
- Focus on documenting the usage of declarative configuration with Spring Boot applications.
- Adjust the example to use the OpenTelemetry Spring Boot Starter instead of the Java Agent.
- Make sure to highlight any differences or important notes specific to the Spring Boot Starter usage of declarative configuration.
- requires 2.22.0+ version of the spring starter
- The auto-suggested properties in IDNs are not correct, because they are based on the non-declarative config schema.
- Use https://github.com/open-telemetry/opentelemetry-configuration for details on schema and examples, but
  indent all examples by two spaces to fit under the "otel:" root node (for Spring Boot declarative config).
- "file_format" serves as opt-in to declarative config
- Spring uses : instead of :- as separator for the default value, e.g. ${OTEL_EXPORTER_OTLP_ENDPOINT:http://localhost:4318}/v1/traces instead of ${OTEL_EXPORTER_OTLP_ENDPOINT:-http://localhost:4318}/v1/traces
- add a link to the application.yaml configuration file in this module
- `configureSampler` in `OpenTelemetryConfig` in the `spring-native` module can serve as an example of programmatic configuration that mimics declarative config behavior to filter out health checks
