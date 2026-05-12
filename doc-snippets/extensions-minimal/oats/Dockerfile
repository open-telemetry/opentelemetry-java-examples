FROM eclipse-temurin:25.0.3_9-jre@sha256:9c9e7c4f5f3840e5254be62ea9a7de56b2d0af23864032a8a3654bf63c31cd5b

WORKDIR /usr/src/app/

# renovate: datasource=github-releases depName=open-telemetry/opentelemetry-java-instrumentation
ENV OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION=v2.27.0

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