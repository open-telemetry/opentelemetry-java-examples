FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the JAR file and agent
COPY build/libs/app.jar /app.jar
COPY build/agent/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

# Expose the port
EXPOSE 8080

# Run the application with the Java agent
ENTRYPOINT ["java", "-javaagent:/opentelemetry-javaagent.jar", "-jar", "/app.jar"]