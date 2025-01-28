plugins {
    id("java")
}

val moduleName by extra { "io.opentelemetry.examples.docs.configuration" }

dependencies {
    implementation("io.opentelemetry:opentelemetry-api")

    implementation("io.opentelemetry.semconv:opentelemetry-semconv")
    implementation("io.opentelemetry.semconv:opentelemetry-semconv-incubating:1.30.0-alpha-rc.1")
}
