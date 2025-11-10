FROM eclipse-temurin:17.0.17_10-jre@sha256:d5ea6f5fa36cf8bc698f1cdf86dc6719a19f4cf20cb539ac681b2c4c511ed029

ADD build/libs/app.jar /app.jar
ADD build/agent/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar
ADD build/agent/opentelemetry-javaagent-extension.jar /opentelemetry-javaagent-extension.jar

ENTRYPOINT java -javaagent:/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/opentelemetry-javaagent-extension.jar -jar /app.jar
