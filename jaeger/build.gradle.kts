plugins {
    id("java")
}

description = "OpenTelemetry Examples for Jaeger Backend"
val moduleName by extra { "io.opentelemetry.examples.jaeger" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")

    //alpha module
    implementation("io.opentelemetry.semconv:opentelemetry-semconv")
}
