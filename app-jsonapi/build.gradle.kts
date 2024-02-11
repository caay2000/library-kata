plugins {
    id("project-library")
    id("plugin-kotlin-serialization")
}

dependencies {
    implementation(project(":libs:common-http"))
    implementation(project(":libs:common-resource-bus"))

    testImplementation(project(":libs:common-test"))
}
