FROM eclipse-temurin:11.0.29_7-jre@sha256:5b337e80d724db86936d5e16790d4a8865350228de6a606de12dcb0e8c64ec42

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090