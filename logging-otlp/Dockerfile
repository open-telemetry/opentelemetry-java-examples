FROM eclipse-temurin:21-jre

WORKDIR /usr/src/app/

COPY build/libs/*SNAPSHOT.jar ./app.jar
# we ignore the version (which is from upstream) and use the latest version of the grafana distribution
# todo:  with renovate update
ADD --chmod=644 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.10.0/opentelemetry-javaagent.jar /usr/src/app/opentelemetry-javaagent.jar
ENV JAVA_TOOL_OPTIONS=-javaagent:/usr/src/app/opentelemetry-javaagent.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]
