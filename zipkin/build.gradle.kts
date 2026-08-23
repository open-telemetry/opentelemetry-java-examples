plugins {
    id("java")
}

description = "OpenTelemetry Example exporting OTLP to a Zipkin backend"
extra.set("moduleName", "io.opentelemetry.examples.zipkin")

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
