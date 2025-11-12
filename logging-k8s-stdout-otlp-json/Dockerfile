FROM eclipse-temurin:21.0.9_10-jre@sha256:293cb3562c37da3ce356e02f9fb91043fa65cb9f38df17519fd02dff4e0e2309

WORKDIR /usr/src/app/

# renovate: datasource=github-releases depName=opentelemetry-java-instrumentation packageName=open-telemetry/opentelemetry-java-instrumentation
ENV OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION=v2.21.0

ADD build/libs/*SNAPSHOT.jar ./app.jar
ADD --chmod=644 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/$OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar
ENV JAVA_TOOL_OPTIONS=-javaagent:./opentelemetry-javaagent.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]
