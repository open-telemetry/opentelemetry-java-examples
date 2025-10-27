FROM eclipse-temurin:11.0.28_6-jre@sha256:f73475871fe677beeeeb19b31ab628f05a63d2a24a97a298eb7f55767d25e0f5

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090