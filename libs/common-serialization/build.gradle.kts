plugins {
    id("project-library")
    id("plugin-kotlin-serialization")
}

dependencies {

    api("io.ktor:ktor-serialization-kotlinx-json")
    api("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:${project.ext["kotlinx_serialization_version"]}")
}
