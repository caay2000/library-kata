plugins {
    id("project-library")
//    id("plugin-kotlin-serialization")
}

dependencies {

    api("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")

//    api("io.ktor:ktor-serialization-kotlinx-json")
//    api("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:${project.ext["kotlinx_serialization_version"]}")
}
