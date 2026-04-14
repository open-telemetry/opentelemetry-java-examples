FROM eclipse-temurin:25.0.2_10-jre@sha256:8f94f991adb7341cb5bcb39e5d7ee4ac944af94e54f5c67e9c8567ed064f0325

ADD build/libs/app.jar /app.jar
ADD build/agent/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/opentelemetry-javaagent-extension.jar -jar /app.jar
