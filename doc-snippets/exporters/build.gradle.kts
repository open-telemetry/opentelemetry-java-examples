import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("java")
    id("org.springframework.boot") version "4.0.0"
}

val moduleName by extra { "io.opentelemetry.examples.docs.exporters" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.22.0"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-prometheus")
    implementation("io.opentelemetry:opentelemetry-exporter-zipkin")
    implementation("io.opentelemetry.semconv:opentelemetry-semconv")
}
