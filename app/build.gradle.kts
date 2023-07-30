plugins {
    id("project-application")
    id("project-context")
    id("plugin-kotlin-serialization")
}
dependencies {
    implementation("io.ktor:ktor-server-call-logging-jvm:2.3.2")
    implementation("io.ktor:ktor-server-call-id-jvm:2.3.2")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}
