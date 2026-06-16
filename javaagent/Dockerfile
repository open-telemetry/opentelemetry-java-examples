FROM eclipse-temurin:25.0.3_9-jre@sha256:5cf92df78f6dba978777d5cffa3c856e583f86814fde82a6c3534ccdfd794f2f

ADD build/libs/app.jar /app.jar
ADD build/agent/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/opentelemetry-javaagent-extension.jar -jar /app.jar
