FROM eclipse-temurin:11.0.27_6-jre@sha256:176adb5b3504ef4f6c31b4019e690129381fa2d6f43f63b01138ddb4bc394426

ADD build/libs/opentelemetry-examples-prometheus-*-SNAPSHOT-all.jar /app.jar

ENTRYPOINT java -cp app.jar io.opentelemetry.example.prometheus.PrometheusExample 19090