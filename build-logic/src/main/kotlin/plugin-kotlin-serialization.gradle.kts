plugins {
    id("project-library")
    kotlin("plugin.serialization")
}

dependencies{
    api("io.ktor:ktor-server-content-negotiation")
    api("io.ktor:ktor-serialization-kotlinx-json")
    api("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm")
}
