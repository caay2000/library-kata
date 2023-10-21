import gradle.kotlin.dsl.accessors._d35a649ed5a5db03cdbfc0be61d44559.ext
import gradle.kotlin.dsl.accessors._d35a649ed5a5db03cdbfc0be61d44559.implementation

plugins {
    id("project-common")
    `java-library`
}

dependencies {
    implementation(project(":libs:common-arrow"))
    implementation(project(":libs:common-cqrs"))
    implementation(project(":libs:common-database"))
    implementation(project(":libs:common-date"))
    implementation(project(":libs:common-http"))
    implementation(project(":libs:common-id-generator"))
    implementation(project(":libs:common-jsonapi"))
    implementation(project(":libs:lib-dependency-injection"))
    implementation(project(":libs:lib-memory-database"))

    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-call-logging")

    implementation("io.ktor:ktor-server-swagger")
    implementation("io.ktor:ktor-server-cors")

    implementation("io.arrow-kt:arrow-core")

    implementation("io.github.microutils:kotlin-logging-jvm")

    testImplementation(project(":libs:common-test"))
}
