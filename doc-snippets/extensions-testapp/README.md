# Extensions - Test Application

This is a simple HTTP server application used for testing OpenTelemetry Java agent extensions. It provides one endpoint:

- `GET /hello` - Returns a simple greeting

## Building

To build the application JAR:

```bash
cd /path/to/opentelemetry-java-examples
./gradlew :doc-snippets:extensions-testapp:jar
```

The output JAR will be at: `build/libs/extensions-testapp-1.0-SNAPSHOT.jar`

## Running

To run the application with the OpenTelemetry Java agent:

```bash
java -javaagent:path/to/opentelemetry-javaagent.jar \
     -Dotel.service.name=test-app \
     -jar build/libs/extensions-testapp-1.0-SNAPSHOT.jar
```

The server will start on port 8080. You can test it with:

```bash
curl http://localhost:8080/hello
```

## Usage with Extensions

This application is designed to be used with the extensions-minimal module to test custom OpenTelemetry extensions.
See the extensions-minimal readme for details on running OATS tests.
