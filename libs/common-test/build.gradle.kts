plugins {
    id("project-library")
}

dependencies {
    implementation(project(":libs:common-date"))
    implementation(project(":libs:common-event"))
    implementation(project(":libs:common-http"))
    implementation(project(":libs:common-id-generator"))
    implementation(project(":libs:common-jsonapi"))

    api("com.networknt:json-schema-validator:1.0.87")
    api(kotlin("test"))
    api("io.github.microutils:kotlin-logging-jvm")
    api("org.assertj:assertj-core")
    api("org.skyscreamer:jsonassert")
    api("io.kotest.extensions:kotest-assertions-arrow")
    api("io.ktor:ktor-server-test-host")
    api("org.awaitility:awaitility-kotlin")
}
