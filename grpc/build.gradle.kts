import com.google.protobuf.gradle.*

plugins {
    id("java")
    id ("com.google.protobuf")
    id ("idea")
}

description = "OpenTelemetry Examples for GRPC"
val moduleName by extra { "io.opentelemetry.examples.grpc" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

var grpcVersion = "1.60.1" // CURRENT_GRPC_VERSION
var protobufVersion = "3.25.1"
var protocVersion = protobufVersion

dependencies {
    runtimeOnly ("io.grpc:grpc-netty-shaded:1.59.1")
    implementation ("io.grpc:grpc-protobuf:1.59.1")
    implementation ("io.grpc:grpc-services:1.59.1")
    implementation ("io.grpc:grpc-stub:1.59.1")
    implementation ("com.google.protobuf:protobuf-java-util:3.24.0")
    implementation ("io.opentelemetry.instrumentation:opentelemetry-grpc-1.6:1.32.0-alpha")
    implementation ("com.google.guava:guava:32.0.1-jre")

    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-zipkin")
    implementation("io.opentelemetry.instrumentation:opentelemetry-grpc-1.6")

    //alpha modules
    implementation("io.opentelemetry.semconv:opentelemetry-semconv")

    implementation("com.google.j2objc:j2objc-annotations:2.8")
    implementation("org.apache.tomcat:annotations-api:6.0.53")
}

//protobuf {
//    protoc {
//        artifact = "com.google.protobuf:protoc:${protobufVersion}"
//    }
//    plugins {
//        id("grpc") {
//            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
//        }
//    }
//    generateProtoTasks {
//        ofSourceSet("main").forEach {
//            it.plugins {
//                // Apply the "grpc" plugin whose spec is defined above, without
//                // options. Note the braces cannot be omitted, otherwise the
//                // plugin will not be added. This is because of the implicit way
//                // NamedDomainObjectContainer binds the methods.
//                id("grpc") { }
//            }
//        }
//    }
//}
//
//sourceSets {
//    main {
//        proto {
//            srcDir '../submodules/durabletask-protobuf/protos'
//        }
//        java {
//            srcDirs 'build/generated/source/proto/main/grpc'
//            srcDirs 'build/generated/source/proto/main/java'
//        }
//    }
//}

protobuf {
    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:${protobufVersion}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
    }
    generateProtoTasks {
        all().configureEach {
            plugins {
                id("grpc")
            }
        }
    }
}
