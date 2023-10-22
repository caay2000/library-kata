package com.github.caay2000.librarykata.hexagonal.configuration

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.ktor.server.application.Application
import io.ktor.server.application.install

fun Application.configureOpenApiDocumentation() {
    // TODO use a different, more known library for generating openapi documentation
    // currently using https://github.com/SMILEY4/ktor-swagger-ui
    install(SwaggerUI) {
        swagger {
            swaggerUrl = "docs"
            forwardRoot = false
        }
        info {
            title = "Library Kata - Ktor"
            description = "Library Kata - Ktor"
        }
    }
}
