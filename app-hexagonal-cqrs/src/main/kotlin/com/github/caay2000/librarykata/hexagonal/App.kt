package com.github.caay2000.librarykata.hexagonal

import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.idgenerator.UUIDGenerator
import com.github.caay2000.librarykata.hexagonal.configuration.DependencyInjectionConfiguration
import com.github.caay2000.librarykata.hexagonal.configuration.RoutingConfiguration
import com.github.caay2000.librarykata.hexagonal.configuration.ShutdownHookConfiguration
import com.github.caay2000.librarykata.hexagonal.configuration.StartupHookConfiguration
import com.github.caay2000.librarykata.hexagonal.configuration.configureOpenApiDocumentation
import com.github.caay2000.librarykata.hexagonal.configuration.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callloging.CallLogging
import mu.KotlinLogging

fun Application.main() {
    module()
}

fun Application.module() {
    install(CallId) { generate { UUIDGenerator().generate() } }
    install(CallLogging) {
        callIdMdc("correlationId")
        // TODO print duration of request
        // check ktor callLogging format and https://youtrack.jetbrains.com/issue/KTOR-1250/Log-HTTP-request-time
        logger = KotlinLogging.logger(Controller::class.java.canonicalName)
    }
    install(StartupHookConfiguration)
    install(ShutdownHookConfiguration)
    install(DependencyInjectionConfiguration)
    install(RoutingConfiguration)
    configureOpenApiDocumentation()
    configureSerialization()
}
