FROM eclipse-temurin:25.0.1_8-jre@sha256:96bccca442e8842a7366df338f5960e3342862ea6b8d42b97009a3dfd8602547

ADD build/libs/app.jar /app.jar
ADD build/agent/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/opentelemetry-javaagent-extension.jar -jar /app.jar
