import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java")
    id("org.springframework.boot") version "4.0.0"
}

java {
    toolchain {
        // due to Spring Boot 3 requirement, not because of declarative config support
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

description = "OpenTelemetry Java Agent Declarative Configuration Example"
val moduleName by extra { "io.opentelemetry.examples.javaagent.declarative" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
}

tasks.named<BootJar>("bootJar") {
    archiveFileName = "javaagent-declarative-configuration.jar"
}
