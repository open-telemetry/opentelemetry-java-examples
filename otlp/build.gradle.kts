plugins {
    id("java")
    id("application")
}

description = "OpenTelemetry Example for OTLP Exporters"
val moduleName by extra { "io.opentelemetry.examples.otlp" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

application {
    mainClass = "io.opentelemetry.example.otlp.OtlpExporterExample"
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("io.opentelemetry.semconv:opentelemetry-semconv")
}
