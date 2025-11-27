FROM eclipse-temurin:25.0.1_8-jre@sha256:73fe03b88c2a48ec41f9a86428c65ea5913229488ca218cd6a4f55f8cb0ece1f

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT exec java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090
