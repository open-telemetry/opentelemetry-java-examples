plugins {
    id("java")
}

description = "OpenTelemetry Examples for SDK Usage"
val moduleName by extra { "io.opentelemetry.examples.sdk.usage" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-logging")
}
