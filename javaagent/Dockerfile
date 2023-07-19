FROM eclipse-temurin:11.0.19_7-jre

ADD build/libs/opentelemetry-examples-javaagent-*-SNAPSHOT.jar /app.jar
ADD build/otel/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

ENTRYPOINT java -jar -javaagent:/opentelemetry-javaagent.jar app.jar