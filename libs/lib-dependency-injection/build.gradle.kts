plugins {
    id("project-library")
    id("plugin-kotlin-logging")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation(project(":libs:common-test"))
}
