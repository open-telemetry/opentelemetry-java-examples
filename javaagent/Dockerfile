FROM eclipse-temurin:25.0.2_10-jre@sha256:f47c82ca0abea08c1c92affd40d2e07976fae85836050e9e4a5702055d5c0e3f

ADD build/libs/app.jar /app.jar
ADD build/agent/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/opentelemetry-javaagent-extension.jar -jar /app.jar
