plugins {
    id("java")
}

val moduleName by extra { "io.opentelemetry.examples.docs.prometheus.migration" }

dependencies {
    implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.22.0"))

    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-prometheus")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")

    implementation("io.prometheus:prometheus-metrics-core:1.3.1")
    implementation("io.prometheus:prometheus-metrics-exporter-httpserver:1.3.1")
    implementation("io.prometheus:prometheus-metrics-exporter-opentelemetry:1.3.1")
}
