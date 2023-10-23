plugins {
    id("project-library")
    id("plugin-kotlin-serialization")
}

dependencies {
    implementation(project(":libs:common-serialization"))
    implementation("io.swagger.core.v3:swagger-annotations:2.2.17")
}
