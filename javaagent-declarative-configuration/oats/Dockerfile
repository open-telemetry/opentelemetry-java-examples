FROM eclipse-temurin:25.0.4_7-jre@sha256:f9e65324a37f28209ce7dd0e5149a7aa954520ed936fb87813cf6ded2400a112

WORKDIR /usr/src/app/

# renovate: datasource=github-releases depName=open-telemetry/opentelemetry-java-instrumentation
ENV OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION=v2.31.1

ADD ./build/libs/javaagent-declarative-configuration.jar ./app.jar
ADD --chmod=644 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/$OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar
ADD ./otel-agent-config.yaml ./otel-agent-config.yaml
ENV JAVA_TOOL_OPTIONS=-javaagent:./opentelemetry-javaagent.jar
ENV OTEL_EXPERIMENTAL_CONFIG_FILE=/usr/src/app/otel-agent-config.yaml

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]