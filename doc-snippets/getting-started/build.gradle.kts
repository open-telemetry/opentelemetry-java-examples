import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("java")
    id("org.springframework.boot") version "3.3.2"
}

val moduleName by extra { "io.opentelemetry.examples.docs.getting-started" }

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.6.0"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}
