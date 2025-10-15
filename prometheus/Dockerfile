FROM eclipse-temurin:11.0.28_6-jre@sha256:12e6611e8bdd47af0a12574f69c178745b09998459ccbac2ec2b08e837bce1a7

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090