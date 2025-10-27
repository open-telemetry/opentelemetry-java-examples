FROM eclipse-temurin:17.0.16_8-jre@sha256:696817d4fb79b9c442e84e18483d8345dd2d0b6710546344e5eb182bd3153333

ADD build/libs/app.jar /app.jar
ADD build/agent/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar
ADD build/agent/opentelemetry-javaagent-extension.jar /opentelemetry-javaagent-extension.jar

ENTRYPOINT java -javaagent:/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/opentelemetry-javaagent-extension.jar -jar /app.jar
