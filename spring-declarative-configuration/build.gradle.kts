import org.gradle.kotlin.dsl.named
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java")
    id("org.springframework.boot") version "3.5.10"
}

description = "OpenTelemetry Example for Spring Boot with Declarative Configuration"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.25.0"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter")
}

tasks.named<BootJar>("bootJar") {
    archiveFileName = "spring-declarative-configuration.jar"
}

