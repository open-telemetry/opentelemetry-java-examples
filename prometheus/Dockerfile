FROM eclipse-temurin:11.0.29_7-jre@sha256:8bfe305a356ce178f983de347a10397fd489ed853799af07d025eb1c7306c905

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090