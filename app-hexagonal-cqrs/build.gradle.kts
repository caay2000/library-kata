plugins {
    id("project-application")
    id("project-hexagonal-cqrs")
    id("plugin-kotlin-serialization")
}
dependencies {
    implementation("io.ktor:ktor-server-call-logging-jvm:${project.ext["ktor_version"]}")
    implementation("io.ktor:ktor-server-call-id-jvm:${project.ext["ktor_version"]}")
    implementation("io.github.smiley4:ktor-swagger-ui:2.6.0")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.16")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}

jib {
    container.mainClass = "com.github.caay2000.librarykata.hexagonal.AppKt.module"
    to.image = "caay2000/library-kata-hexagonal-cqrs:${project.version}"
}
