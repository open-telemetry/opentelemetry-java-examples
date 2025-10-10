FROM eclipse-temurin:11.0.28_6-jre@sha256:a6296fe50db155baaa429abc3a5f01faecf29cab383c2a90c2221b398f155b1d

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090