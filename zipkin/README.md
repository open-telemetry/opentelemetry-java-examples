# Zipkin Example

This is a simple example that demonstrates how to use the OpenTelemetry SDK
to instrument a simple application and send spans over OTLP to a Zipkin backend.

Note: the OpenTelemetry Java SDK no longer ships a Zipkin exporter
(`opentelemetry-exporter-zipkin` stopped being published in 1.65.0), so this
example exports OTLP and relies on Zipkin's own OTLP ingestion. Upstream
`openzipkin/zipkin` does not include an OTLP collector, so the
[zipkin-otel](https://github.com/openzipkin-contrib/zipkin-otel) distribution is
used below. It binds OTLP/HTTP endpoints to Zipkin's regular server port, 9411.

## How to Run

### Prerequisites

* Java 17 or higher is required to run Gradle and build this example
* Java 8 or higher may be used to run the example once it has been built
* Docker 19.03

## 1 - Compile

```shell script
../gradlew shadowJar
```

## 2 - Run Zipkin

```shell script
docker run --rm -it --name zipkin-otel \
  -p 9411:9411 \
  ghcr.io/openzipkin-contrib/zipkin-otel:latest
```

Verify that the OTLP collector is running:

```shell script
curl -s localhost:9411/health
```

The response should include `"OpenTelemetryHttpCollector{}": {"status": "UP"}`.

## 3 - Start the Application

```shell script
java -cp build/libs/opentelemetry-examples-zipkin-0.1.0-SNAPSHOT-all.jar io.opentelemetry.example.zipkin.ZipkinExample localhost 9411
```

## 4 - Open the Zipkin UI

Navigate to <http://localhost:9411/zipkin> and click on search.
