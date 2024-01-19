package com.github.caay2000.librarykata.core

import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.idgenerator.UUIDGenerator
import com.github.caay2000.librarykata.core.configuration.ShutdownHookConfiguration
import com.github.caay2000.librarykata.core.configuration.StartupHookConfiguration
import com.github.caay2000.librarykata.core.configuration.configureOpenApiDocumentation
import com.github.caay2000.librarykata.core.configuration.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callloging.CallLogging
import mu.KotlinLogging
import com.github.caay2000.librarykata.core.configuration.cqrs.DependencyInjectionConfiguration as CRQRSDependencyInjectionConfiguration
import com.github.caay2000.librarykata.core.configuration.cqrs.RoutingConfiguration as CQRSRoutingConfiguration

fun Application.main() {
    module()
}

fun Application.module() {
    val codeArchitecture = "CQRS"

    install(CallId) { generate { UUIDGenerator().generate() } }
    install(CallLogging) {
        callIdMdc("correlationId")
        // TODO print duration of request
        // check ktor callLogging format and https://youtrack.jetbrains.com/issue/KTOR-1250/Log-HTTP-request-time
        logger = KotlinLogging.logger(Controller::class.java.canonicalName)
    }
    install(StartupHookConfiguration)
    install(ShutdownHookConfiguration)
    if (codeArchitecture == "CQRS")
        {
            install(CRQRSDependencyInjectionConfiguration)
            install(CQRSRoutingConfiguration)
        }

    configureOpenApiDocumentation()
    configureSerialization()
}
