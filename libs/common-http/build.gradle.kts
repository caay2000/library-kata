plugins {
    id("project-library")
    id("plugin-kotlin-serialization")
}

dependencies {
    api(project(":libs:common-serialization"))
//    api(project(":libs:common-jsonapi"))

    implementation("io.ktor:ktor-server-core:${project.ext["ktor_version"]}")
    implementation("io.ktor:ktor-server-netty:${project.ext["ktor_version"]}")
    implementation("io.ktor:ktor-server-content-negotiation:${project.ext["ktor_version"]}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${project.ext["ktor_version"]}")

    implementation("io.github.microutils:kotlin-logging-jvm")

    api("io.github.smiley4:ktor-swagger-ui:2.6.0")
    api("io.swagger.core.v3:swagger-annotations:2.2.17")
}
