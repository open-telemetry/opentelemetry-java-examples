plugins {
    id("java")
}

description = "OpenTelemetry Examples for SDK autoconfiguration"
val moduleName by extra { "io.opentelemetry.examples.autoconfigure" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-exporter-logging")
    implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure")
}
