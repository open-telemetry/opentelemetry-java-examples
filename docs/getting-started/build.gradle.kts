import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("java")
    id("org.springframework.boot") version "3.2.5"
}

val moduleName by extra { "io.opentelemetry.examples.docs.getting-started" }

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation(platform("io.opentelemetry:opentelemetry-bom:1.37.0"))
    implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom-alpha:2.3.0-alpha"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}