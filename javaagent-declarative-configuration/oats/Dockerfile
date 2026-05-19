FROM eclipse-temurin:25.0.3_9-jre@sha256:04262e8782d6b034ee5d7c1c5d4e8938fcf2063a76b4bfcd84e5d994d09c27bc

WORKDIR /usr/src/app/

# renovate: datasource=github-releases depName=open-telemetry/opentelemetry-java-instrumentation
ENV OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION=v2.28.0

ADD ./build/libs/javaagent-declarative-configuration.jar ./app.jar
ADD --chmod=644 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/$OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar
ADD ./otel-agent-config.yaml ./otel-agent-config.yaml
ENV JAVA_TOOL_OPTIONS=-javaagent:./opentelemetry-javaagent.jar
ENV OTEL_EXPERIMENTAL_CONFIG_FILE=/usr/src/app/otel-agent-config.yaml

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]