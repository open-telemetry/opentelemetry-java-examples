# HTTP Example

**Note:** This is an advanced scenario useful for people who want to *manually* instrument their own code.

This is a simple example that demonstrates how to use the OpenTelemetry SDK
to *manually* instrument a simple HTTP-based client/server application.
The example creates the **root span** on the client and sends the context
over the HTTP request. On the server side, the example shows how to extract the context
and create a **child span** with an attached **span event**.

## How to Run

### Prerequisites

* Java 17 or higher is required to run Gradle and build this example
* Java 8 or higher may be used to run the example once it has been built

## 1 - Compile

```shell script
../gradlew shadowJar
```

## 2 - Start the Server

```shell script
java -cp ./build/libs/opentelemetry-examples-http-0.1.0-SNAPSHOT-all.jar io.opentelemetry.example.http.HttpServer
```

## 3 - Start the Client

```shell script
java -cp ./build/libs/opentelemetry-examples-http-0.1.0-SNAPSHOT-all.jar io.opentelemetry.example.http.HttpClient
```
