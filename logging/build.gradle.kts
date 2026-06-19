plugins {
    id("java")
}

description = "OpenTelemetry Examples for logging exporters"
extra.set("moduleName", "io.opentelemetry.examples.logging")

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-exporter-logging")
}
