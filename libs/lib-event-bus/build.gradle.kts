plugins {
    id("project-library")
    id("plugin-kotlin-logging")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    testImplementation(project(":libs:common-test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
}
