plugins {
    id("project-library")
    id("plugin-kotlin-serialization")
}

dependencies {
    implementation(project(":app-jsonapi"))

    implementation(project(":libs:common-date"))
    implementation(project(":libs:common-arrow"))
    implementation(project(":libs:common-ddd"))
    implementation(project(":libs:common-http"))
    implementation(project(":libs:common-id-generator"))

    api("com.networknt:json-schema-validator:1.1.0")
    api(kotlin("test"))
    api("io.github.microutils:kotlin-logging-jvm")
    api("org.assertj:assertj-core")
    api("org.skyscreamer:jsonassert")
    api("io.kotest.extensions:kotest-assertions-arrow")
    api("io.ktor:ktor-server-test-host")
    api("org.awaitility:awaitility-kotlin")
}
