import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java")
    id("org.springframework.boot") version "3.5.6"
}

description = "OpenTelemetry Java Agent Declarative Configuration Example"
val moduleName by extra { "io.opentelemetry.examples.javaagent.declarative" }

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
}

val copyExtension = tasks.register<Copy>("copyExtension") {
    into(layout.buildDirectory.dir("agent"))
    rename(".*\\.jar", "opentelemetry-javaagent-extension.jar")
}

tasks.named<BootJar>("bootJar") {
    dependsOn(copyExtension)
    archiveFileName = "javaagent-declarative-configuration.jar"
}
