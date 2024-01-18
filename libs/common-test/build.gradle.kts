plugins {
    id("project-library")
    id("plugin-kotlin-logging")
    id("plugin-kotlin-serialization")
}

dependencies {

    implementation(project(":app-jsonapi"))
    implementation(project(":libs:common-date"))
    implementation(project(":libs:common-http"))
    implementation(project(":libs:common-id-generator"))

    api(kotlin("test"))
    api("com.networknt:json-schema-validator")
    api("org.assertj:assertj-core")
    api("org.skyscreamer:jsonassert")
    api("io.kotest.extensions:kotest-assertions-arrow")
    api("io.ktor:ktor-server-test-host")
    api("org.awaitility:awaitility-kotlin")
}
