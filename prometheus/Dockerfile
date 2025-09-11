FROM eclipse-temurin:11.0.28_6-jre@sha256:b78d72dae5e186b8afd33a4c70b82713aa9e3911aa1edbf8b075a0ce0faee97e

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090