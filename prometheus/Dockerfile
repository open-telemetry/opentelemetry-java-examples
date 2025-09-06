FROM eclipse-temurin:11.0.28_6-jre@sha256:3cb109ccbd7756214c84995a5f216177be77a1ecafbb7bed3ed473e2611a82ab

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090