FROM eclipse-temurin:25.0.3_9-jre@sha256:5cf92df78f6dba978777d5cffa3c856e583f86814fde82a6c3534ccdfd794f2f

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT exec java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090
