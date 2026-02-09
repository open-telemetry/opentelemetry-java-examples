FROM eclipse-temurin:25.0.2_10-jre@sha256:ad53b918fedaefd44135a226c5ecaa4828a3d72db2f6d520da96a62979a63ac6

ADD build/libs/app.jar /app.jar
ADD build/agent/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/opentelemetry-javaagent-extension.jar -jar /app.jar
