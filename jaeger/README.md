# Jaeger Example

This is a simple example that demonstrates how to use the OpenTelemetry SDK
to instrument a simple application and export to a Jaeger backend.

# How to Run

## Prerequisites

* Java 17 or higher is required to build this example (to run Gradle)
* Java 8 or higher is required to run the compiled example
* Docker 19.03
* Jaeger 1.16 or higher

## 1 - Compile 
```shell script
../gradlew shadowJar
```
## 2 - Run Jaeger

```shell script
docker run --rm -it --name jaeger\
  -e COLLECTOR_OTLP_ENABLED=true \
  -p 4317:4317 \
  -p 16686:16686 \
  jaegertracing/all-in-one:1.39
```


## 3 - Start the Application
```shell script
java -cp build/libs/opentelemetry-examples-jaeger-0.1.0-SNAPSHOT-all.jar io.opentelemetry.example.jaeger.JaegerExample http://localhost:4317
```
## 4 - Open the Jaeger UI

Navigate to http://localhost:16686

[jaeger]: https://www.jaegertracing.io/docs/1.16/getting-started/
