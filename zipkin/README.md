# Zipkin Example

This is a simple example that demonstrates how to use the OpenTelemetry SDK
to instrument a simple application using Zipkin as the trace exporter.

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
docker run --rm -it --name zipkin \
  -p 9411:9411 \
  openzipkin/zipkin:latest
```

## 3 - Start the Application

```shell script
java -cp build/libs/opentelemetry-examples-zipkin-0.1.0-SNAPSHOT-all.jar io.opentelemetry.example.zipkin.ZipkinExample localhost 9411
```

## 4 - Open the Zipkin UI

Navigate to <http://localhost:9411/zipkin> and click on search.
