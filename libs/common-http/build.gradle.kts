plugins {
    id("project-library")
    id("plugin-kotlin-serialization")
}

dependencies {
    api(project(":libs:common-serialization"))

    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")

    implementation("io.github.microutils:kotlin-logging-jvm")

    api("io.github.smiley4:ktor-swagger-ui:2.7.4")
    api("io.swagger.core.v3:swagger-annotations:2.2.20")
}
