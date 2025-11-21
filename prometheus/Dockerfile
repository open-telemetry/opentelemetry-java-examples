FROM eclipse-temurin:11.0.29_7-jre@sha256:5a9b558567e6a2f7fbaa5000891caf515c31dbc5ce9ca8c3d61ef8eb864a0da4

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT exec java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090
