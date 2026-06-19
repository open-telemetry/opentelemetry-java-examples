plugins {
    id("java")
}

extra.set("moduleName", "io.opentelemetry.examples.docs.prometheus.migration")

dependencies {
    implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.29.0"))

    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-prometheus")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")

    implementation("io.prometheus:prometheus-metrics-core:1.8.0")
    implementation("io.prometheus:prometheus-metrics-exporter-httpserver:1.8.0")
    implementation("io.prometheus:prometheus-metrics-exporter-opentelemetry:1.8.0")
}
