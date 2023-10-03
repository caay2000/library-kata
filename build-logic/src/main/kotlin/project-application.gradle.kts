plugins {
    id("project-common")
    id("com.google.cloud.tools.jib")
    application
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}
