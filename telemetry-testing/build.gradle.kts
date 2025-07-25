import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("java")
    id("org.springframework.boot") version "3.5.4"
}

description = "OpenTelemetry Example for Telemetry Testing"
val moduleName by extra { "io.opentelemetry.examples.telemetry-testing" }

val bootRun = tasks.named<BootRun>("bootRun") {
    mainClass = "io.opentelemetry.example.javagent.Application"
}

val agent = configurations.create("agent")

val bootJar = tasks.named<BootJar>("bootJar") {
    dependsOn(agent)
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:2.18.1")
    //spring modules
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation(enforcedPlatform("org.junit:junit-bom:5.13.4"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.mock-server:mockserver-netty:5.15.0")
    testImplementation("org.awaitility:awaitility:4.3.0")
    testImplementation("io.opentelemetry.proto:opentelemetry-proto:1.7.0-alpha")
    testImplementation("org.assertj:assertj-core:3.27.3")

    agent("io.opentelemetry.javaagent:opentelemetry-javaagent:2.18.1")
}

tasks.test {
    useJUnitPlatform()

    // Add opentelemetry Java Agent jar to JVM args
    // otel.exporter.otlp.protocol - the transport protocol to use on OTLP trace, metric, and log requests
    // otel.metric.export.interval - the interval, in milliseconds, between the start of two export attempts
    // More information: https://github.com/open-telemetry/opentelemetry-java/blob/main/sdk-extensions/autoconfigure/README.md
    //
    // mockserver.logLevel=off is needed to avoid circular exporting of log records
    jvmArgs = listOf("-javaagent:${agent.singleFile}", "-Dotel.metric.export.interval=5000", "-Dmockserver.logLevel=off")
}
