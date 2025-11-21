import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("java")
    id("org.springframework.boot") version "4.0.0"
}

description = "OpenTelemetry Example for Telemetry Testing"
val moduleName by extra { "io.opentelemetry.examples.telemetry-testing" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
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
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:2.22.0")
    //spring modules
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-resttestclient")

    testImplementation(enforcedPlatform("org.junit:junit-bom:6.0.1"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.mock-server:mockserver-netty:5.15.0")
    testImplementation("org.awaitility:awaitility:4.3.0")
    testImplementation("io.opentelemetry.proto:opentelemetry-proto:1.9.0-alpha")
    testImplementation("org.assertj:assertj-core:3.27.6")

    agent("io.opentelemetry.javaagent:opentelemetry-javaagent:2.22.0")
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
