plugins {
    id("project-application")
    id("project-core")
    id("plugin-kotlin-serialization")
}

dependencies {
    implementation(project(":app-hexagonal-cqrs"))
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}

jib {
    container.mainClass = "com.github.caay2000.librarykata.hexagonal.AppKt.module"
    to.image = "caay2000/library-kata-hexagonal-cqrs:${project.version}"
}
