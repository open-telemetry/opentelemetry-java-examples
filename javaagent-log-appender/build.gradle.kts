plugins {
    id("java")
    id("application")
}

description = "OpenTelemetry Log Appender Example using JavaAgent"
val moduleName by extra { "io.opentelemetry.examples.javaagent-log-appender" }

java {
    toolchain {
        // logback 1.4.x+ requires java 11
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

val agent = configurations.create("agent")

dependencies {
    // Slf4J / logback
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-core:1.5.9")
    implementation("ch.qos.logback:logback-classic:1.5.9")

    // JUL to SLF4J bridge
    implementation("org.slf4j:jul-to-slf4j:2.0.16")

    // Log4j
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.24.1"))
    implementation("org.apache.logging.log4j:log4j-api")
    implementation("org.apache.logging.log4j:log4j-core")

    // OpenTelemetry core
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry.semconv:opentelemetry-semconv")

    // OpenTelemetry log4j / logback appenders
    implementation("io.opentelemetry.instrumentation:opentelemetry-log4j-appender-2.17")
    implementation("io.opentelemetry.instrumentation:opentelemetry-logback-appender-1.0")

    // OpenTelemetry JavaAgent
    agent("io.opentelemetry.javaagent:opentelemetry-javaagent:2.8.0")
}

application {
    mainClass = "io.opentelemetry.example.logappender.Application"
}


tasks.named<JavaExec>("run") {
    doFirst {
        jvmArgs("-javaagent:${agent.singleFile}")
    }
}
