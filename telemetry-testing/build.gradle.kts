import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("java")
    id("org.springframework.boot") version "2.7.17"
    id("io.spring.dependency-management") version "1.1.4"
}

description = "OpenTelemetry Example for Telemetry Testing"
val moduleName by extra { "io.opentelemetry.examples.telemetry-testing" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

val bootRun = tasks.named<BootRun>("bootRun") {
    mainClass = "io.opentelemetry.example.javagent.Application"
}

val agent = configurations.create("agent")

val bootJar = tasks.named<BootJar>("bootJar") {
    dependsOn(agent)
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:1.31.0")
    //spring modules
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation(enforcedPlatform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.mock-server:mockserver-netty:5.15.0:shaded")
    testImplementation("org.awaitility:awaitility:4.2.0")
    testImplementation("io.opentelemetry.proto:opentelemetry-proto:0.14.0-alpha")
    testImplementation("org.assertj:assertj-core:3.24.2")

    agent("io.opentelemetry.javaagent:opentelemetry-javaagent:1.31.0")
}

tasks.test {
    useJUnitPlatform()

    // Add opentelemetry Java Agent jar to JVM args
    // otel.exporter.otlp.protocol - the transport protocol to use on OTLP trace, metric, and log requests
    // otel.metric.export.interval - the interval, in milliseconds, between the start of two export attempts
    // More information: https://github.com/open-telemetry/opentelemetry-java/blob/main/sdk-extensions/autoconfigure/README.md
    jvmArgs = listOf("-javaagent:${agent.singleFile}", "-Dotel.exporter.otlp.protocol=http/protobuf", "-Dotel.metric.export.interval=5000")
}
