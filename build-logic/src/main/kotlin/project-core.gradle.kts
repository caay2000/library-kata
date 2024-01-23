import gradle.kotlin.dsl.accessors._11edc5ca6fa1b1db1cca8a72d7d24483.ext
import gradle.kotlin.dsl.accessors._11edc5ca6fa1b1db1cca8a72d7d24483.implementation

plugins {
    `java-library`
    id("project-common")
    id("plugin-kotlin-logging")
    id("plugin-kotlin-serialization")
}

dependencies {
    implementation(project(":app-jsonapi"))

    implementation(project(":libs:common-arrow"))
    implementation(project(":libs:common-cqrs"))
    implementation(project(":libs:common-date"))
    implementation(project(":libs:common-http"))
    implementation(project(":libs:common-id-generator"))
    implementation(project(":libs:lib-dependency-injection"))
    implementation(project(":libs:lib-memory-database"))

    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-call-id-jvm")
    implementation("io.ktor:ktor-server-double-receive")

    testImplementation(project(":libs:common-test"))
}
