FROM eclipse-temurin:21.0.8_9-jre@sha256:2f696f62306d28d32cce2ea6bbb2f2f5b8df736adcc019be61d0f90ce88f07d5

WORKDIR /usr/src/app/

# renovate: datasource=github-releases depName=opentelemetry-java-instrumentation packageName=open-telemetry/opentelemetry-java-instrumentation
ENV OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION=v2.19.0

ADD build/libs/*SNAPSHOT.jar ./app.jar
ADD --chmod=644 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/$OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar
ENV JAVA_TOOL_OPTIONS=-javaagent:./opentelemetry-javaagent.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]
