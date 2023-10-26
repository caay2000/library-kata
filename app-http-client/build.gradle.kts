plugins {
    id("project-library")
    id("plugin-kotlin-serialization")
}
dependencies {
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-apache5")
    implementation("io.ktor:ktor-client-logging")
    implementation("io.ktor:ktor-client-content-negotiation")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}
