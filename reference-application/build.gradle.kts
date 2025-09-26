import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("java")
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.6"
}

val moduleName by extra { "io.opentelemetry.examples.reference-application" }

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.20.1"))
    
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    
    // OpenTelemetry SDK and API
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure")
    
    // OpenTelemetry Exporters
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("io.opentelemetry:opentelemetry-exporter-logging")
    implementation("io.opentelemetry:opentelemetry-exporter-prometheus")
    
    // OpenTelemetry Instrumentation - use manual configuration instead of starter
    implementation("io.opentelemetry.instrumentation:opentelemetry-logback-appender-1.0")
    
    // Micrometer for additional metrics
    implementation("io.micrometer:micrometer-registry-prometheus")
    
    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.opentelemetry:opentelemetry-sdk-testing")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}