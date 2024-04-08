plugins {
    id("java")
}

description = "OpenTelemetry Examples for Manual Tracing"
val moduleName by extra { "io.opentelemetry.examples.http" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-logging")

    //alpha modules
    implementation("io.opentelemetry.semconv:opentelemetry-semconv")
    implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure")
    implementation("io.opentelemetry:opentelemetry-api-incubator")
}
