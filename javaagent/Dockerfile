FROM eclipse-temurin:25.0.2_10-jre@sha256:91e59267939a3d14198d537b504af2d836c44894dde19919b2f22192c26841ef

ADD build/libs/app.jar /app.jar
ADD build/agent/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/opentelemetry-javaagent-extension.jar -jar /app.jar
