FROM eclipse-temurin:25.0.5.1_2-jre@sha256:4ed132f6832e9804b123e89a849a7b4502756ae64a4ee08ebab939d9d5d0a9fa

ADD build/libs/app.jar /app.jar
ADD build/agent/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/opentelemetry-javaagent-extension.jar -jar /app.jar
