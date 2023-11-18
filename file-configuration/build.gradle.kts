plugins {
    id("java")
    id("application")
}

description = "OpenTelemetry Example for File Configuration"
val moduleName by extra { "io.opentelemetry.examples.fileconfig" }

dependencies {
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-logging")
    implementation("io.opentelemetry:opentelemetry-sdk-extension-incubator")
}

application {
    mainClass = "io.opentelemetry.examples.fileconfig.Application"
}
