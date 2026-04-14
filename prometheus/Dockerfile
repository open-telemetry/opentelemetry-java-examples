FROM eclipse-temurin:25.0.2_10-jre@sha256:8f94f991adb7341cb5bcb39e5d7ee4ac944af94e54f5c67e9c8567ed064f0325

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT exec java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090
