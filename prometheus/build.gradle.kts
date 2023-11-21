plugins {
    id("java")
}

description = "OpenTelemetry Example for Prometheus Exporter"
val moduleName by extra { "io.opentelemetry.examples.prometheus" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-logging")
    implementation("io.opentelemetry.semconv:opentelemetry-semconv")

    //alpha modules
    implementation("io.opentelemetry:opentelemetry-exporter-prometheus")
}
