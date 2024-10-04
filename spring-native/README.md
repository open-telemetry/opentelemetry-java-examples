# OpenTelemetry Spring Native Example

This example demonstrates usage of the [OpenTelemetry Spring starter](https://opentelemetry.io/docs/instrumentation/java/automatic/spring-boot/) with a Spring Boot application running in a GraalVM native image.

The example uses the following elements:

- A web API available at `GET http://localhost:8080/ping`.
- A docker compose setup configured to run the application and export to
  the [collector](https://opentelemetry.io/docs/collector/) via OTLP.
- A collector configured with
  the [OTLP receiver](https://github.com/open-telemetry/opentelemetry-collector/tree/main/receiver/otlpreceiver)
  and exporting to the standard output with
  the [logging exporter](https://github.com/open-telemetry/opentelemetry-collector/tree/main/exporter/debugexporter).
- A spring configuration to suppress spans for the `/actuator` endpoint
- A spring configuration to set OTLP headers dynamically 
  (not needed for the example - it shows how to configure exporters programmatically)

# Description of the instrumentation set-up

We have included the [OpenTelemetry Spring starter](https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/) to instrument the HTTP calls, the logs, the SQL queries, and send the OpenTelemetry data via OTLP:

```kotlin
 implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter")
```

## Prerequisites

* GraalVM for Java 17
* Docker compose

# How to run

Build the GraalVM native image
```shell
cd spring-native
../gradlew bootBuildImage --imageName=otel-native-graalvm
```

Run the application and the collector with docker compose
```shell
docker-compose up
```

In a separate shell, exercise the application by calling its endpoint
```shell
curl http://localhost:8080/ping
```

Watch for spans, metrics, and logs in the collector log output.

Example of the beginning of the collector log after the ping:
```
graal-native-collector-1  | Span #0
graal-native-collector-1  |     Trace ID       : 668c936f4f0580bac9db40162e8159fa
graal-native-collector-1  |     Parent ID      : 
graal-native-collector-1  |     ID             : 858b9b11341b1bb9
graal-native-collector-1  |     Name           : GET /ping
graal-native-collector-1  |     Kind           : Server
graal-native-collector-1  |     Start time     : 2023-06-09 13:49:43.270616 +0000 UTC
graal-native-collector-1  |     End time       : 2023-06-09 13:49:43.271635862 +0000 UTC
graal-native-collector-1  |     Status code    : Unset
graal-native-collector-1  |     Status message : 
graal-native-collector-1  | Attributes:
graal-native-collector-1  |      -> http.target: Str(/ping)
graal-native-collector-1  |      -> net.sock.peer.addr: Str(172.20.0.1)
graal-native-collector-1  |      -> user_agent.original: Str(curl/8.0.1)
graal-native-collector-1  |      -> net.host.name: Str(localhost)
graal-native-collector-1  |      -> net.sock.host.addr: Str(172.20.0.3)
graal-native-collector-1  |      -> http.route: Str(/ping)
graal-native-collector-1  |      -> net.sock.peer.port: Int(42646)
graal-native-collector-1  |      -> http.method: Str(GET)
graal-native-collector-1  |      -> http.scheme: Str(http)
graal-native-collector-1  |      -> http.status_code: Int(200)
graal-native-collector-1  |      -> net.protocol.version: Str(1.1)
graal-native-collector-1  |      -> net.host.port: Int(8080)
graal-native-collector-1  |      -> net.protocol.name: Str(http)
graal-native-collector-1  |      -> http.response_content_length: Int(4)
graal-native-collector-1  | 	{"kind": "exporter", "data_type": "traces", "name": "logging"}
graal-native-collector-1  | 2023-06-09T13:50:07.529Z	info	MetricsExporter	{"kind": "exporter", "data_type": "metrics", "name": "logging", "resource metrics": 1, "metrics": 3, "data points": 3}
graal-native-collector-1  | 2023-06-09T13:50:07.529Z	info	ResourceMetrics #0
graal-native-collector-1  | Resource SchemaURL: 
graal-native-collector-1  | Resource attributes:
graal-native-collector-1  |      -> service.name: Str(unknown_service:java)
graal-native-collector-1  |      -> telemetry.sdk.language: Str(java)
graal-native-collector-1  |      -> telemetry.sdk.name: Str(opentelemetry)
graal-native-collector-1  |      -> telemetry.sdk.version: Str(1.26.0)
graal-native-collector-1  | ScopeMetrics #0
graal-native-collector-1  | ScopeMetrics SchemaURL: 
graal-native-collector-1  | InstrumentationScope io.opentelemetry.spring-webmvc-6.0 
graal-native-collector-1  | Metric #0
graal-native-collector-1  | Descriptor:
graal-native-collector-1  |      -> Name: http.server.duration
graal-native-collector-1  |      -> Description: The duration of the inbound HTTP request
graal-native-collector-1  |      -> Unit: ms
graal-native-collector-1  |      -> DataType: Histogram
graal-native-collector-1  |      -> AggregationTemporality: Cumulative
graal-native-collector-1  | HistogramDataPoints #0
graal-native-collector-1  | Data point attributes:
graal-native-collector-1  |      -> http.method: Str(GET)
graal-native-collector-1  |      -> http.route: Str(/ping)
graal-native-collector-1  |      -> http.scheme: Str(http)
graal-native-collector-1  |      -> http.status_code: Int(200)
graal-native-collector-1  |      -> net.host.name: Str(localhost)
graal-native-collector-1  |      -> net.host.port: Int(8080)
graal-native-collector-1  |      -> net.protocol.name: Str(http)
graal-native-collector-1  |      -> net.protocol.version: Str(1.1)
graal-native-collector-1  | StartTimestamp: 2023-06-09 13:49:07.528013 +0000 UTC
graal-native-collector-1  | Timestamp: 2023-06-09 13:50:07.528308 +0000 UTC
graal-native-collector-1  | Count: 1
graal-native-collector-1  | Sum: 0.991699
graal-native-collector-1  | Min: 0.991699
graal-native-collector-1  | Max: 0.991699
```

We can see below an example of instrumented log:

```
spring-native-collector-1  | LogRecord #2
spring-native-collector-1  | ObservedTimestamp: 2023-11-24 11:49:49.956067 +0000 UTC
spring-native-collector-1  | Timestamp: 2023-11-24 11:49:49.955 +0000 UTC
spring-native-collector-1  | SeverityText: INFO
spring-native-collector-1  | SeverityNumber: Info(9)
spring-native-collector-1  | Body: Str(Started Application in 0.119 seconds (process running for 0.122))
```

You see below the span created by the database instrumentation:

```
spring-native-collector-1  | ScopeSpans #0
spring-native-collector-1  | ScopeSpans SchemaURL:
spring-native-collector-1  | InstrumentationScope io.opentelemetry.jdbc
spring-native-collector-1  | Span #0
spring-native-collector-1  |     Trace ID       : 0727c2fe8ea3d7c76ae56c58eeddc894
spring-native-collector-1  |     Parent ID      :
spring-native-collector-1  |     ID             : 918a7df29e0ab097
spring-native-collector-1  |     Name           : db
spring-native-collector-1  |     Kind           : Client
spring-native-collector-1  |     Start time     : 2023-11-24 16:02:21.883235 +0000 UTC
spring-native-collector-1  |     End time       : 2023-11-24 16:02:21.8834037 +0000 UTC
spring-native-collector-1  |     Status code    : Unset
spring-native-collector-1  |     Status message :
spring-native-collector-1  | Attributes:
spring-native-collector-1  |      -> db.name: Str(db)
spring-native-collector-1  |      -> db.connection_string: Str(h2:mem:)
spring-native-collector-1  |      -> db.statement: Str(create table test_table (id bigint not null, primary key (id)))
spring-native-collector-1  |      -> db.system: Str(h2)
```
