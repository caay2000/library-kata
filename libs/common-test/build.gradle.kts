plugins {
    id("project-library")
}

dependencies {
    implementation(project(":libs:common-date"))
    implementation(project(":libs:common-event"))
    implementation(project(":libs:common-http"))
    api(project(":libs:common-serialization"))
    implementation(project(":libs:common-id-generator"))

    api(kotlin("test"))
    api("io.github.microutils:kotlin-logging-jvm")
    api("org.assertj:assertj-core")
    api("org.skyscreamer:jsonassert")
    api("io.kotest.extensions:kotest-assertions-arrow")
    api("io.ktor:ktor-server-test-host")
    api("org.awaitility:awaitility-kotlin")
}
