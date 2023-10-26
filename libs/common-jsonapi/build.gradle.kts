plugins {
    id("project-library")
    id("plugin-kotlin-serialization")
}

dependencies {
    implementation(project(":libs:common-serialization"))
    api("io.github.smiley4:ktor-swagger-ui:2.6.0")
    api("io.swagger.core.v3:swagger-annotations:2.2.17")
}
