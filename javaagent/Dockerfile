FROM eclipse-temurin:17.0.17_10-jre@sha256:b7193e67b90eff065773be4aa6ec94b729ab8c71a8291697b90547cc562856c1

ADD build/libs/app.jar /app.jar
ADD build/agent/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar
ADD build/agent/opentelemetry-javaagent-extension.jar /opentelemetry-javaagent-extension.jar

ENTRYPOINT java -javaagent:/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/opentelemetry-javaagent-extension.jar -jar /app.jar
