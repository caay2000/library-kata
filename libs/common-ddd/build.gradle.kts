plugins {
    id("project-library")
}

dependencies {

    api(project(":libs:common-arrow"))
    api(project(":libs:common-http"))
    api(project(":libs:lib-event-bus"))

    implementation("io.arrow-kt:arrow-fx-coroutines")
    implementation("io.github.microutils:kotlin-logging-jvm")
}
