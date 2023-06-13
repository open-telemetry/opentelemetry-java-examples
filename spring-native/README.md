# OpenTelemetry Java Agent Example

This example demonstrates usage of the [OpenTelemetry Spring starter](https://github.com/open-telemetry/opentelemetry-java-instrumentation/tree/main/instrumentation/spring/starters/spring-boot-starter) with a Spring Boot application and a GraalVM native image.

It consists of a Spring Boot application with:

- A simple web API available at `GET http://localhost:8080/ping`. When called,
  the OpenTelemetry Spring starter records spans and metrics.
- A docker compose setup configured to run the application and export to
  the [collector](https://opentelemetry.io/docs/collector/) via OTLP.
- The collector is configured with
  the [OTLP receiver](https://github.com/open-telemetry/opentelemetry-collector/tree/main/receiver/otlpreceiver)
  and export it to standard out with
  the [logging exporter](https://github.com/open-telemetry/opentelemetry-collector/tree/main/exporter/loggingexporter)

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