plugins {
    id("java")
    id("application")
}

group = "com.example"
description = "OpenTelemetry Example for Azure Resource Detector"
val moduleName by extra { "io.opentelemetry.examples.azure-resource-detector" }

val autoconfConfig = listOf(
    "-Dotel.traces.exporter=logging-otlp",
    "-Dotel.metrics.exporter=none",
    "-Dotel.logs.exporter=none",
    "-Dotel.java.global-autoconfigure.enabled=true"
)

repositories {
  mavenCentral()
  mavenLocal()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

application {
    mainClass = "io.opentelemetry.example.azure.resource.detector.AzureResourceDetectorExample"
    applicationDefaultJvmArgs = autoconfConfig
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api:1.37.0")
    implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure:1.37.0")
    implementation("io.opentelemetry:opentelemetry-exporter-logging-otlp:1.37.0")
    implementation("io.opentelemetry.contrib:opentelemetry-azure-resources:1.34.0-alpha-SNAPSHOT")
}
repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}