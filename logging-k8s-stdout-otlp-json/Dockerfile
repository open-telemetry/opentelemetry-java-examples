FROM eclipse-temurin:21.0.8_9-jre@sha256:66bb900643426ad01996d25bada7d56751913f9cec3b827fcb715d2ec9a0fbfc

WORKDIR /usr/src/app/

# renovate: datasource=github-releases depName=opentelemetry-java-instrumentation packageName=open-telemetry/opentelemetry-java-instrumentation
ENV OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION=v2.21.0

ADD build/libs/*SNAPSHOT.jar ./app.jar
ADD --chmod=644 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/$OPENTELEMETRY_JAVA_INSTRUMENTATION_VERSION/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar
ENV JAVA_TOOL_OPTIONS=-javaagent:./opentelemetry-javaagent.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]
