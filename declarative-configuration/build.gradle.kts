plugins {
    id("java")
    id("application")
}

description = "OpenTelemetry Example for Declarative Configuration"
val moduleName by extra { "io.opentelemetry.examples.fileconfig" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-logging")
    implementation("io.opentelemetry:opentelemetry-sdk-extension-incubator")
}

application {
    mainClass = "io.opentelemetry.examples.fileconfig.Application"
}
