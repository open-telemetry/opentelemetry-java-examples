plugins {
    id("com.diffplug.spotless")
    id("com.github.johnrengelman.shadow") apply false
    id("java-library")
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "java-library")
    apply(plugin = "com.github.johnrengelman.shadow")

    group = "io.opentelemetry"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    dependencies {
        // using the bom ensures that all of your opentelemetry dependency versions are aligned
        implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom-alpha:2.12.0-alpha"))
    }

    spotless {
        java {
            targetExclude("**/generated/**")
            googleJavaFormat()
        }
    }
}
