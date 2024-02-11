plugins {
    id("project-library")
    id("plugin-kotlin-logging")
}

dependencies {
    implementation(project(":libs:common-http"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    testImplementation(project(":libs:common-test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
}
