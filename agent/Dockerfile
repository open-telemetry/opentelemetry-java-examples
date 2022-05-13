FROM adoptopenjdk:11-jre-hotspot

ADD build/libs/opentelemetry-examples-agent-*-SNAPSHOT.jar /app.jar
ADD build/otel/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

ENTRYPOINT java -jar -javaagent:/opentelemetry-javaagent.jar app.jar