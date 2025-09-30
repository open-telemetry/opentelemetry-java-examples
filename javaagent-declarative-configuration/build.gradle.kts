import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java")
    id("org.springframework.boot") version "3.5.6"
}

description = "OpenTelemetry Java Agent Declarative Configuration Example"
val moduleName by extra { "io.opentelemetry.examples.javaagent.declarative" }

val extension = configurations.create("extension")

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    
    extension("io.opentelemetry.contrib:opentelemetry-samplers:1.50.0-alpha") {
        isTransitive = false
    }
}

val copyExtension = tasks.register<Copy>("copyExtension") {
    from(extension.singleFile)
    into(layout.buildDirectory.dir("agent"))
    rename(".*\\.jar", "opentelemetry-javaagent-extension.jar")
}

tasks.named<BootJar>("bootJar") {
    dependsOn(copyExtension)
    archiveFileName = "javaagent-declarative-configuration.jar"
}
