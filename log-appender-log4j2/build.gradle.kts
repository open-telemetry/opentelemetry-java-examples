plugins {
    id("java")
    id("application")
}

description = "OpenTelemetry Log4j2 SDK Example"
val moduleName by extra { "io.opentelemetry.examples.log-appender" }

java {
    toolchain {
        // logback 1.4.x+ requires java 11
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

val agent = configurations.create("agent")

dependencies {
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.25.2"))

    // Slf4J API
    implementation("org.slf4j:slf4j-api:2.0.17")
    // Log4j2 API
    implementation("org.apache.logging.log4j:log4j-api")
    // Log4j1 API (and bridge to SLF4J)
    implementation("org.slf4j:log4j-over-slf4j:2.0.17")
    // OTEL API
    implementation("io.opentelemetry:opentelemetry-api")

    // JUL to SLF4J bridge
    implementation("org.slf4j:jul-to-slf4j:2.0.17")
    // Log4j1 to SLF4J bridge
    implementation("org.slf4j:slf4j-api:2.0.17")

    // SLF4J binding for Log4j2
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl")

    // Log4j2 SDK
    implementation("org.apache.logging.log4j:log4j-core")
    implementation("io.opentelemetry.instrumentation:opentelemetry-log4j-appender-2.17")

    // OTEL SDK
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("io.opentelemetry:opentelemetry-sdk-extension-incubator")

    agent("io.opentelemetry.javaagent:opentelemetry-javaagent:2.23.0-SNAPSHOT")
}

val copyAgent = tasks.register<Copy>("copyAgent") {
    from(agent.singleFile)
    into(layout.buildDirectory.dir("agent"))
    rename("opentelemetry-javaagent-.*\\.jar", "opentelemetry-javaagent.jar")
}

tasks.getByName("run") {
    dependsOn(copyAgent)
}

application {
    mainClass = "io.opentelemetry.example.logappender.Application"

    if (project.hasProperty("runWithAgent")) {
        applicationDefaultJvmArgs = listOf("-javaagent:build/agent/opentelemetry-javaagent.jar")
    }
}

tasks.register<JavaExec>("runWithoutAgent") {
    group = ApplicationPlugin.APPLICATION_GROUP
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass = "io.opentelemetry.example.logappender.Application"
}

tasks.register<JavaExec>("runWithAgent") {
    dependsOn(copyAgent)

    group = ApplicationPlugin.APPLICATION_GROUP
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass = "io.opentelemetry.example.logappender.Application"
    jvmArgs = listOf("-javaagent:build/agent/opentelemetry-javaagent.jar")
}
