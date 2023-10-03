plugins {
    id("project-application")
    id("project-hexagonal-cqrs")
    id("plugin-kotlin-serialization")
}
dependencies {
    implementation("io.ktor:ktor-server-call-logging-jvm:${project.ext["ktor_version"]}")
    implementation("io.ktor:ktor-server-call-id-jvm:${project.ext["ktor_version"]}")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}
