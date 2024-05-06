plugins {
    id("java")
}

description = "OpenTelemetry Examples for metrics"
val moduleName by extra { "io.opentelemetry.examples.metrics" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api")

    implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("io.opentelemetry:opentelemetry-api-incubator")
}
