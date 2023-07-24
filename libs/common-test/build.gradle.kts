plugins {
    id("project-library")
}

dependencies {
    api(kotlin("test"))
    api("org.assertj:assertj-core")
    api("org.skyscreamer:jsonassert")
    api("io.kotest.extensions:kotest-assertions-arrow")
    api("io.ktor:ktor-server-test-host")
    api("org.awaitility:awaitility-kotlin")
}
