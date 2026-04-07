FROM eclipse-temurin:25.0.2_10-jre@sha256:541cd02a3920c1f10b2f34af7b7144414bc48f0b5dbc933a1def87699e5e5727

ADD build/libs/app.jar /app.jar
ADD build/agent/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/opentelemetry-javaagent-extension.jar -jar /app.jar
