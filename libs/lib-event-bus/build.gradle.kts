
plugins {
    id("project-library")
}

dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    implementation("io.github.microutils:kotlin-logging-jvm")
    implementation("ch.qos.logback:logback-classic")

    testImplementation(project(":libs:common-test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
}
