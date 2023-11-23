plugins {
    id ("org.jetbrains.kotlin.jvm") version "1.9.21"
}

description = "OpenTelemetry Example for Kotlin extensions"

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-extension-kotlin")
    implementation("io.opentelemetry:opentelemetry-sdk-testing")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}