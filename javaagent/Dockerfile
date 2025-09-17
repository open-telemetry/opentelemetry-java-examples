FROM eclipse-temurin:17.0.16_8-jre@sha256:a17a2d28a7100a63f512fb9db86fa3ce8d577f1e6f6abbf8da1cad1bf48ece87

ADD build/libs/app.jar /app.jar
ADD build/agent/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar
ADD build/agent/opentelemetry-javaagent-extension.jar /opentelemetry-javaagent-extension.jar

ENTRYPOINT java -javaagent:/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/opentelemetry-javaagent-extension.jar -jar /app.jar
