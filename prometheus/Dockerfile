FROM eclipse-temurin:25.0.3_9-jre@sha256:671061ae5a8f0d6827816e15bc39d21e224c43ff96998a97cd0d3989c4d17815

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT exec java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090
