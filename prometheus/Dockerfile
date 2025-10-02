FROM eclipse-temurin:11.0.28_6-jre@sha256:7d00fb8d3ecff458d660ea172fb7d6dfb35ce796e3f503c41f77d354c92af080

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090