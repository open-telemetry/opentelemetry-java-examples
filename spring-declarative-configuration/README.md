# Spring Boot Declarative Configuration Example

This example demonstrates how to
use [declarative configuration](https://opentelemetry.io/docs/specs/otel/configuration/#declarative-configuration)
with the OpenTelemetry Spring Boot Starter to configure tracing, metrics, and logging for a Spring
Boot application.

Instead of using the OpenTelemetry Java Agent, this module uses the
[OpenTelemetry Spring Boot Starter](https://github.com/open-telemetry/opentelemetry-java-instrumentation/tree/main/instrumentation/spring/spring-boot-autoconfigure)
and configures declarative behavior via standard Spring Boot configuration in `application.yaml`.

The main configuration file for this example is:

- [`src/main/resources/application.yaml`](./src/main/resources/application.yaml)

For the underlying declarative configuration schema and additional examples, see the
[opentelemetry-configuration](https://github.com/open-telemetry/opentelemetry-configuration)
repository.
Remember that when you copy examples from that repository into a Spring Boot app, you must nest them
under the `otel:` root node (two-space indentation for all keys shown below).

This Spring Boot application includes two endpoints:

- `/actuator/health` – A health check endpoint (from Spring Boot Actuator) that is configured to be
  **excluded** from tracing
- `/api/example` – A simple API endpoint that **will be traced** normally

## End-to-End Instructions

### Prerequisites

- Java 17 or higher
- OpenTelemetry Spring Boot Starter **2.22.0+** on the classpath of this application (already
  configured in this example’s [build file](./build.gradle.kts))

### Step 1: Build the Application

From this `spring-declarative-configuration` directory:

```bash
../gradlew bootJar
```

This builds the Spring Boot fat JAR for the example application.

### Step 2: Run the Spring Boot Application

Run the application as a normal Spring Boot JAR – no `-javaagent` flag and no separate
`otel-agent-config.yaml` file
are needed. Declarative configuration is picked up from `application.yaml` by the Spring Boot
Starter.

```bash
java -jar build/libs/spring-declarative-configuration.jar
```

The Spring Boot Starter will automatically:

- Initialize OpenTelemetry SDK
- Read declarative configuration under the `otel:` root in `application.yaml`
- Apply exporters, processors, and sampler rules defined there

### Step 3: Test the Endpoints

In a separate terminal, call both endpoints:

```bash
# This endpoint will NOT be traced (excluded by declarative configuration)
curl http://localhost:8080/actuator/health

# This endpoint WILL be traced normally
curl http://localhost:8080/api/example
```

### Step 4: Verify Tracing, Metrics, and Logs

By default, this example configures:

- An OTLP HTTP exporter for traces, metrics, and logs
- A console exporter for traces for easy local inspection

Check the application logs to see that:

- Health check requests (`/actuator/health`) **do not** generate spans (dropped by sampler rules)
- Requests to `/api/example` **do** generate spans and are exported to console and/or OTLP

If you have an OTLP-compatible backend (e.g., the OpenTelemetry Collector, Jaeger, Tempo, etc.)
listening on
`http://localhost:4318`, you can inspect the exported telemetry there as well.

## Declarative Configuration with Spring Boot

The declarative configuration used by this example lives in [`application.yaml`](./src/main/resources/application.yaml)
under the `otel:` root key.

```yaml
otel:
# ... see src/main/resources/application.yaml for the full configuration
```

This layout follows the declarative configuration schema defined in the
[opentelemetry-configuration](https://github.com/open-telemetry/opentelemetry-configuration)
repository, but adapted for Spring Boot:

- All OpenTelemetry configuration keys live under the `otel:` root
- Configuration blocks from the reference repo (such as `tracer_provider`, `meter_provider`,
  `logger_provider`, etc.) are indented by **two spaces** beneath `otel:`
- The configuration is loaded via the OpenTelemetry Spring Boot Starter instead of the Java Agent

### Opting In with `file_format`

Declarative configuration is **opt-in**. In this Spring Boot example, you enable declarative
configuration by setting `file_format` under `otel:` in `application.yaml`:

```yaml
otel:
  file_format: "1.0-rc.2"
  # ... other configuration
```

The `file_format` value follows the versions defined in the
[declarative configuration specification](https://github.com/open-telemetry/opentelemetry-configuration).
If `file_format` is missing, declarative configuration is not applied.

### Example: Exporters and Sampler Rules (Spring Style)

Below is a simplified view of the configuration used in this module. All keys are indented under
`otel:` as required by Spring Boot declarative configuration. Refer to the actual
[`application.yaml`](./src/main/resources/application.yaml) for the complete version.

```yaml
otel:
  file_format: "1.0-rc.2"

  tracer_provider:
    sampler:
      rule_based_routing:
        fallback_sampler:
          always_on:
        span_kind: SERVER
        rules:
          - action: DROP
            attribute: url.path
            pattern: /actuator.*
```

This configuration:
- Uses the `rule_based_routing` sampler from the OpenTelemetry contrib extension
- Excludes health check endpoints (`/actuator.*`) from tracing using the `DROP` action
- Samples all other requests using the `always_on` fallback sampler
- Only applies to `SERVER` span kinds

## Spring Boot Starter–Specific Notes

### Spring Boot Starter Version

- Declarative configuration is supported by the OpenTelemetry Spring Boot Starter starting with
  version **2.22.0**
- Ensure your dependencies use at least this version; otherwise, `file_format` and other declarative
  config features may be ignored

### Property Metadata and IDE Auto-Completion

Most IDEs derive auto-completion for Spring properties from Spring Boot configuration metadata. At
the time of this example, that metadata is primarily based on the **non-declarative** configuration 
schema.

As a result:

- Auto-suggested properties in IDEs may be incomplete or incorrect for declarative configuration
  under `otel:`
- Some declarative configuration keys may not appear in auto-completion at all

When in doubt:

- Prefer the official schema and examples in
  [opentelemetry-configuration](https://github.com/open-telemetry/opentelemetry-configuration)
- Then adapt those examples by nesting them under the `otel:` root in your `application.yaml`

### Placeholder Default Values: `:` vs `:-`

Spring Boot’s property placeholder syntax differs slightly from generic examples you might see in
OpenTelemetry docs.

- Generic examples sometimes use `${VAR_NAME:-default}` for default values
- **Spring Boot uses `:` instead of `:-`**

For example, in this module we configure the OTLP HTTP trace endpoint as:

```yaml
otel:
  tracer_provider:
    processors:
      - batch:
          exporter:
            otlp_http:
              endpoint: ${OTEL_EXPORTER_OTLP_ENDPOINT:http://localhost:4318}/v1/traces
```

Here, `http://localhost:4318` is used as the default if the `OTEL_EXPORTER_OTLP_ENDPOINT` 
environment variable is not set.

When copying configuration from non-Spring examples, always convert `:-` to `:` in placeholders.

## Declarative vs Programmatic Configuration

Declarative configuration, as used in this example, allows you to express routing and sampling rules
entirely in configuration files. This is ideal for:

- Operational teams that need to adjust sampling or filtering without changing code
- Environments where configuration is managed externally (Kubernetes ConfigMaps, Spring Cloud
  Config, etc.)

For more advanced or dynamic scenarios, you can still use **programmatic** configuration. The
`spring-native` module in
this repository contains an example of this:

- See `configureSampler` in
  [`OpenTelemetryConfig`](../spring-native/src/main/java/io/opentelemetry/example/graal/OpenTelemetryConfig.java)
- It uses `RuleBasedRoutingSampler` programmatically to drop spans for actuator endpoints 
  (`/actuator*`), replicating the behavior we achieve declaratively via YAML in this module

In many cases, you can start with declarative configuration (as in this module) and only fall back
to programmatic customization for highly dynamic or application-specific logic.

## Troubleshooting and Tips

If the behavior is not what you expect, here are a few things to check:

- **Health checks are still traced**
    - Verify the `rules` section under `otel.tracer_provider.sampler.rule_based_routing` in
      `application.yaml`
    - Ensure the `pattern` matches your actual actuator paths (e.g., `/actuator.*`)
    - Confirm that `span_kind` is set to `SERVER` (or another correct span kind for your traffic)

- **No spans are exported**
    - Confirm that `otel.file_format` is set correctly (for example, `"1.0-rc.2"`)
    - Check that at least one exporter is configured (e.g., `otlp_http` or `console`)
    - Look for startup warnings or errors related to OpenTelemetry configuration

- **Properties seem to be ignored**
    - Make sure you are modifying the correct `application.yaml` for the active Spring profile
    - Verify that all configuration keys are indented correctly under the `otel:` root
    - Double-check that any placeholders use `:` for defaults (e.g.,
      `${OTEL_EXPORTER_OTLP_ENDPOINT:http://localhost:4318}`)

If issues persist, compare your configuration to:

- This module’s [`application.yaml`](./src/main/resources/application.yaml)
- The Java Agent example in [`javaagent-declarative-configuration`](../javaagent-declarative-configuration)
- The reference schemas and examples in
  [opentelemetry-configuration](https://github.com/open-telemetry/opentelemetry-configuration)
