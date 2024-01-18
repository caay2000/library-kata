plugins {
    id("project-library")
    id("plugin-kotlin-serialization")
}

dependencies {

    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-content-negotiation")

    api("io.ktor:ktor-serialization-kotlinx-json")
    api("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:${project.ext["kotlinx_serialization_version"]}")

    implementation("io.github.microutils:kotlin-logging-jvm")

    api("io.github.smiley4:ktor-swagger-ui:2.7.4")
    api("io.swagger.core.v3:swagger-annotations:2.2.20")
}
