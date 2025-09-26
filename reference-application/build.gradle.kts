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