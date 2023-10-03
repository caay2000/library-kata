plugins {
    id("project-application")
    id("project-event-driven")
    id("plugin-kotlin-serialization")
}
dependencies {
    implementation("io.ktor:ktor-server-call-logging-jvm:${project.ext["ktor_version"]}")
    implementation("io.ktor:ktor-server-call-id-jvm:${project.ext["ktor_version"]}")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}

jib {
    container.mainClass = "com.github.caay2000.librarykata.eventdriven.AppKt.module"
    to.image = "caay2000/library-kata-event-driven:${project.version}"
}
