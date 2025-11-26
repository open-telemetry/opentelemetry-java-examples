FROM eclipse-temurin:21.0.9_10-jre@sha256:4332b7939ba5b7fabde48f4da21ebe45a4f8943d5b3319720c321ac577e65fb1

WORKDIR /usr/src/app/

# renovate: datasource=github-releases depName=open-telemetry/opentelemetry-java-instrumentation
ENV OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION=v2.22.0

ADD ./build/libs/javaagent-declarative-configuration.jar ./app.jar
ADD --chmod=644 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/$OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar
ADD ./otel-agent-config.yaml ./otel-agent-config.yaml
ENV JAVA_TOOL_OPTIONS=-javaagent:./opentelemetry-javaagent.jar
ENV OTEL_EXPERIMENTAL_CONFIG_FILE=/usr/src/app/otel-agent-config.yaml

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]