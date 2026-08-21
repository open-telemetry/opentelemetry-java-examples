plugins {
    id("java")
}

description = "OpenTelemetry Examples for Zipkin Exporter"
extra.set("moduleName", "io.opentelemetry.examples.zipkin")

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-zipkin:1.64.0")

    //alpha module
    implementation("io.opentelemetry.semconv:opentelemetry-semconv")
}
