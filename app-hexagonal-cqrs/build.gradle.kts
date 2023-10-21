plugins {
    id("project-application")
    id("project-hexagonal-cqrs")
    id("plugin-kotlin-serialization")
}
dependencies {
    implementation("io.ktor:ktor-server-call-logging-jvm:${project.ext["ktor_version"]}")
    implementation("io.ktor:ktor-server-call-id-jvm:${project.ext["ktor_version"]}")

    implementation("io.bkbn:kompendium-core:latest.release")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}

jib {
    container.mainClass = "com.github.caay2000.librarykata.hexagonal.AppKt.module"
    to.image = "caay2000/library-kata-hexagonal-cqrs:${project.version}"
}
