FROM eclipse-temurin:17.0.16_8-jre@sha256:d572f98b3201d3b5a060a08eb489f7bd31184db4d52f3f96d7bb9a69b4a79911

ADD build/libs/app.jar /app.jar
ADD build/agent/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar
ADD build/agent/opentelemetry-javaagent-extension.jar /opentelemetry-javaagent-extension.jar

ENTRYPOINT java -javaagent:/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/opentelemetry-javaagent-extension.jar -jar /app.jar
