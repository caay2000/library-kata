plugins {
    id("project-application")
    id("project-context")
    id("plugin-kotlin-serialization")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}

// dependencies {
//    implementation(project(":libs:common-http"))
//    implementation(project(":libs:common-ddd"))
//    implementation(project(":libs:common-event"))
//    implementation(project(":libs:common-id-generator"))
//    implementation(project(":libs:lib-memory-database"))
//    implementation(project(":libs:lib-dependency-injection"))
//
//    implementation("io.ktor:ktor-server-core")
//    implementation("io.ktor:ktor-server-netty")
//    implementation("io.ktor:ktor-server-content-negotiation")
//    implementation("io.ktor:ktor-serialization-kotlinx-json")
//    implementation("io.ktor:ktor-server-call-logging")
//
//    implementation("io.arrow-kt:arrow-core")
//
//    implementation("io.github.microutils:kotlin-logging-jvm")
// }
