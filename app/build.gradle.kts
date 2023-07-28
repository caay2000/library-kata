plugins {
    id("project-application")
    id("project-context")
    id("plugin-kotlin-serialization")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}
