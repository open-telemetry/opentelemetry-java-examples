import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java")
    id("org.springframework.boot") version "3.5.4"
}

description = "OpenTelemetry Example for Java Agent"
val moduleName by extra { "io.opentelemetry.examples.javagent" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

val agent = configurations.create("agent")
val extension = configurations.create("extension")

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("io.opentelemetry:opentelemetry-api")

    //spring modules
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    agent("io.opentelemetry.javaagent:opentelemetry-javaagent:2.18.1")
    extension("io.opentelemetry.contrib:opentelemetry-samplers:1.48.0-alpha") {
        isTransitive = false
    }
}

val copyAgent = tasks.register<Copy>("copyAgent") {
    from(agent.singleFile)
    into(layout.buildDirectory.dir("agent"))
    rename("opentelemetry-javaagent-.*\\.jar", "opentelemetry-javaagent.jar")
}

val copyExtension = tasks.register<Copy>("copyExtension") {
    from(extension.singleFile)
    into(layout.buildDirectory.dir("agent"))
    rename(".*\\.jar", "opentelemetry-javaagent-extension.jar")
}

tasks.named<BootJar>("bootJar") {
    dependsOn(copyAgent)
    dependsOn(copyExtension)

    archiveFileName = "app.jar"
}
