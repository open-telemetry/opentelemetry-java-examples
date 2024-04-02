plugins {
    id("java")
    id("application")
}

description = "OpenTelemetry Example for Google Cloud Resource Detection"
val moduleName by extra { "io.opentelemetry.resource.gcp" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

application {
    mainClass = "io.opentelemetry.resource.gcp.GCPResourceExample"
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}