plugins {
    id("project-library")
    id("plugin-kotlin-serialization")
}

dependencies {
//    api(project(":libs:common-serialization"))

    implementation("io.ktor:ktor-server-core:${project.ext["ktor_version"]}")
    implementation("io.ktor:ktor-server-netty:${project.ext["ktor_version"]}")
    implementation("io.ktor:ktor-server-content-negotiation:${project.ext["ktor_version"]}")
    implementation("io.ktor:ktor-serialization-jackson:${project.ext["ktor_version"]}")

    implementation("io.github.microutils:kotlin-logging-jvm")
}
