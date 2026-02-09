FROM eclipse-temurin:25.0.2_10-jre@sha256:ad53b918fedaefd44135a226c5ecaa4828a3d72db2f6d520da96a62979a63ac6

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT exec java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090
