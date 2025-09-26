import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java")
    id("org.springframework.boot") version "3.5.6"
}

val moduleName by extra { "io.opentelemetry.examples.reference-application" }

repositories {
    mavenCentral()
}

val agent = configurations.create("agent")

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("io.opentelemetry:opentelemetry-api")
    
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    
    // Micrometer for additional metrics
    implementation("io.micrometer:micrometer-registry-prometheus")
    
    // Java agent
    agent("io.opentelemetry.javaagent:opentelemetry-javaagent:2.20.1")
    
    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.opentelemetry:opentelemetry-sdk-testing")
    
    // Telemetry testing dependencies
    testImplementation(enforcedPlatform("org.junit:junit-bom:5.13.4"))
    testImplementation("org.mockserver:mockserver-netty:5.15.0")
    testImplementation("org.awaitility:awaitility:4.3.0")
    testImplementation("io.opentelemetry.proto:opentelemetry-proto:1.8.0-alpha")
    testImplementation("org.assertj:assertj-core:3.27.6")
}

val copyAgent = tasks.register<Copy>("copyAgent") {
    from(agent.singleFile)
    into(layout.buildDirectory.dir("agent"))
    rename("opentelemetry-javaagent-.*\\.jar", "opentelemetry-javaagent.jar")
}

tasks.named<BootJar>("bootJar") {
    dependsOn(copyAgent)
    archiveFileName = "app.jar"
}

tasks.withType<Test> {
    useJUnitPlatform()
    
    // Add OpenTelemetry Java Agent for telemetry testing
    dependsOn(copyAgent)
    jvmArgs = listOf(
        "-javaagent:${layout.buildDirectory.dir("agent").file("opentelemetry-javaagent.jar").get().asFile.absolutePath}",
        "-Dotel.metric.export.interval=5000",
        "-Dotel.exporter.otlp.protocol=http/protobuf",
        "-Dmockserver.logLevel=off"
    )
}

task("e2eTest", Exec::class) {
    group = "verification"
    description = "Run end-to-end test using docker-compose"
    commandLine("./test-e2e.sh")
    dependsOn("bootJar")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}