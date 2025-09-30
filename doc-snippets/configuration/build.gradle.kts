plugins {
    id("java")
}

val moduleName by extra { "io.opentelemetry.examples.docs.configuration" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure")

    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("io.opentelemetry:opentelemetry-exporter-logging")
    implementation("io.opentelemetry:opentelemetry-exporter-logging-otlp")
    implementation("io.opentelemetry:opentelemetry-exporter-zipkin")
    implementation("io.opentelemetry:opentelemetry-exporter-prometheus")
    implementation("io.opentelemetry:opentelemetry-sdk-extension-jaeger-remote-sampler")

    implementation("io.opentelemetry.semconv:opentelemetry-semconv")
    implementation("io.opentelemetry.semconv:opentelemetry-semconv-incubating:1.37.0-alpha")
}
