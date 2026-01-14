FROM eclipse-temurin:25.0.1_8-jre@sha256:968f1917fcfa8250e72c39c68118798a5c86923a709c174578179b318f9033e6

WORKDIR /usr/src/app/

# renovate: datasource=github-releases depName=open-telemetry/opentelemetry-java-instrumentation
ENV OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION=v2.23.0

ADD ./build/libs/javaagent-declarative-configuration.jar ./app.jar
ADD --chmod=644 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/$OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar
ADD ./otel-agent-config.yaml ./otel-agent-config.yaml
ENV JAVA_TOOL_OPTIONS=-javaagent:./opentelemetry-javaagent.jar
ENV OTEL_EXPERIMENTAL_CONFIG_FILE=/usr/src/app/otel-agent-config.yaml

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]