plugins {
    id("project-library")
    id("plugin-kotlin-serialization")
}

dependencies {

    api("io.ktor:ktor-serialization-kotlinx-json")
    api("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm")
    api("io.github.microutils:kotlin-logging-jvm")

    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-content-negotiation")

    api("io.github.smiley4:ktor-swagger-ui:2.7.4")
    api("io.swagger.core.v3:swagger-annotations:2.2.20")
}
