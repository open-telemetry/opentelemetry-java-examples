FROM eclipse-temurin:21.0.9_10-jre@sha256:bbcd43c7120c41cc5c9fc844b4403e6865a6f911fec1624ff5dda6f9118cc115

WORKDIR /usr/src/app/

# renovate: datasource=github-releases depName=opentelemetry-java-instrumentation packageName=open-telemetry/opentelemetry-java-instrumentation
ENV OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION=v2.21.0

ADD build/libs/*SNAPSHOT.jar ./app.jar
ADD --chmod=644 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/$OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar
ENV JAVA_TOOL_OPTIONS=-javaagent:./opentelemetry-javaagent.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]
