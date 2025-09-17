FROM eclipse-temurin:11.0.28_6-jre@sha256:08b79671648aa82b31e9e7ae82421f14349b3c45cae84abd7bb68a0c52fe56c8

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090