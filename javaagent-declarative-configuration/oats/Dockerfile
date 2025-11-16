FROM eclipse-temurin:21.0.9_10-jre@sha256:ec8d7a0075fe95f0aaccf70243875420294a6c949f86a5efe45a0e943183292b

WORKDIR /usr/src/app/

# renovate: datasource=github-releases depName=opentelemetry-java-instrumentation
ENV OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION=v2.21.0

ADD ./build/libs/javaagent-declarative-configuration.jar ./app.jar
ADD --chmod=644 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/$OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar
ADD ./otel-agent-config.yaml ./otel-agent-config.yaml
ENV JAVA_TOOL_OPTIONS=-javaagent:./opentelemetry-javaagent.jar
ENV OTEL_EXPERIMENTAL_CONFIG_FILE=/usr/src/app/otel-agent-config.yaml

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]