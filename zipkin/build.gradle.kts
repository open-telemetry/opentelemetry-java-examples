plugins {
    id("java")
}

description = "OpenTelemetry Examples for Zipkin Exporter"
val moduleName by extra { "io.opentelemetry.examples.zipkin" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-zipkin")

    //alpha module
    implementation("io.opentelemetry.semconv:opentelemetry-semconv")
}
