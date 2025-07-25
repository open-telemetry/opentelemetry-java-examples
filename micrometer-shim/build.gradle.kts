import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("java")
    id("org.springframework.boot") version "3.5.4"
}

description = "OpenTelemetry Example for Micrometer Shim"
val moduleName by extra { "io.opentelemetry.examples.micrometer-shim" }

val bootRun = tasks.named<BootRun>("bootRun") {
    mainClass = "io.opentelemetry.example.micrometer.Application"
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")

    //alpha modules
    implementation("io.opentelemetry.instrumentation:opentelemetry-micrometer-1.5")
    implementation("io.opentelemetry:opentelemetry-exporter-prometheus")

    //spring modules
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")
}
