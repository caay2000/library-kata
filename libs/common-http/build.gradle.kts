plugins {
    id("project-library")
    id("plugin-kotlin-logging")
    id("plugin-kotlin-serialization")
}

dependencies {
    api("io.github.smiley4:ktor-swagger-ui")
    api("io.swagger.core.v3:swagger-annotations")
}
