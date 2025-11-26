plugins {
    id("java")
    id("application")
    id("com.google.cloud.tools.jib")
}

description = "OpenTelemetry Example for Google Cloud Resource Detection"
val moduleName by extra { "io.opentelemetry.examples.resource-detection.gcp" }

val autoconfConfig = listOf(
    "-Dotel.traces.exporter=logging-otlp",
    "-Dotel.metrics.exporter=none",
    "-Dotel.logs.exporter=none",
    "-Dotel.java.global-autoconfigure.enabled=true",
    "-Dotel.service.name=opentelemetry-examples-resource-gcp",
)

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

application {
    mainClass = "io.opentelemetry.resource.gcp.GCPResourceExample"
    applicationDefaultJvmArgs = autoconfConfig
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure")
    implementation("io.opentelemetry:opentelemetry-exporter-logging-otlp")
    implementation("io.opentelemetry.contrib:opentelemetry-gcp-resources:1.52.0-alpha")
}

jib {
    from.image = "gcr.io/distroless/java-debian10:11"
    containerizingMode = "packaged"
    container.jvmFlags = autoconfConfig
}
