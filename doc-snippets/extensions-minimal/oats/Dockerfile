FROM eclipse-temurin:25.0.3_9-jre@sha256:04262e8782d6b034ee5d7c1c5d4e8938fcf2063a76b4bfcd84e5d994d09c27bc

WORKDIR /usr/src/app/

# renovate: datasource=github-releases depName=open-telemetry/opentelemetry-java-instrumentation
ENV OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION=v2.28.1

# Add the test application JAR
ADD ./extensions-testapp/build/libs/extensions-testapp-1.0-SNAPSHOT.jar ./app.jar

# Add the custom extension JAR
ADD ./extensions-minimal/build/libs/extensions-minimal-0.1.0-SNAPSHOT-all.jar ./extensions/

# Download the OpenTelemetry Java agent
ADD --chmod=644 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/$OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar

# Configure the Java agent with the custom extension
ENV JAVA_TOOL_OPTIONS=-javaagent:./opentelemetry-javaagent.jar
ENV OTEL_JAVAAGENT_EXTENSIONS=./extensions/extensions-minimal-0.1.0-SNAPSHOT-all.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]