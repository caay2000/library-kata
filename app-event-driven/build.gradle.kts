plugins {
    id("project-library")
    id("project-event-driven")
    id("plugin-kotlin-serialization")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}
