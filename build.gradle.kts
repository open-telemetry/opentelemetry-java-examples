import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.diffplug.spotless")
    id("com.gradleup.shadow") apply false
    id("java-library")
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "java-library")
    apply(plugin = "com.gradleup.shadow")

    group = "io.opentelemetry"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    dependencies {
        // using the bom ensures that all of your opentelemetry dependency versions are aligned
        implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom-alpha:2.31.1-alpha"))
    }

    spotless {
        java {
            targetExclude("**/generated/**")
            googleJavaFormat()
        }
    }

    pluginManager.withPlugin("com.gradleup.shadow") {
        tasks.withType<ShadowJar>().configureEach {
            // avoid warning about duplicate kotlin module files being silently dropped
            filesMatching("META-INF/*.kotlin_module") {
                duplicatesStrategy = DuplicatesStrategy.INCLUDE
            }
        }
    }
}
