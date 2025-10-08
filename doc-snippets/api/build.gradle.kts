plugins {
    id("java")
}

val moduleName by extra { "io.opentelemetry.examples.docs.configuration" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api")

    implementation("io.opentelemetry.semconv:opentelemetry-semconv")
    implementation("io.opentelemetry.semconv:opentelemetry-semconv-incubating:1.37.0-alpha")
}
