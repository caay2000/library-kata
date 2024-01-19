package com.github.caay2000.librarykata.core

import com.github.caay2000.librarykata.core.configuration.ShutdownHookConfiguration
import com.github.caay2000.librarykata.core.configuration.configureOpenApiDocumentation
import com.github.caay2000.librarykata.core.configuration.configureSerialization
import com.github.caay2000.librarykata.core.configuration.requestLoggingConfiguration
import com.github.caay2000.librarykata.core.configuration.startupHookConfiguration
import io.ktor.server.application.Application
import io.ktor.server.application.install
import com.github.caay2000.librarykata.core.configuration.cqrs.DependencyInjectionConfiguration as CRQRDependencyInjectionConfiguration
import com.github.caay2000.librarykata.core.configuration.cqrs.RoutingConfiguration as CQRSRoutingConfiguration
import com.github.caay2000.librarykata.core.configuration.event.DependencyInjectionConfiguration as EventDependencyInjectionConfiguration
import com.github.caay2000.librarykata.core.configuration.event.RoutingConfiguration as EventRoutingConfiguration

fun Application.main() {
    module()
}

fun Application.module() {
    // TODO ADD HOCON Configuration to decide Architecture
    val architecture = Architecture.EVENT

    install(startupHookConfiguration(architecture))
    install(ShutdownHookConfiguration)
    when (architecture) {
        Architecture.CQRS -> {
            install(CRQRDependencyInjectionConfiguration)
            install(CQRSRoutingConfiguration)
        }

        Architecture.EVENT -> {
            install(EventDependencyInjectionConfiguration)
            install(EventRoutingConfiguration)
        }
    }

    requestLoggingConfiguration()
    configureOpenApiDocumentation()
    configureSerialization()
}

internal enum class Architecture {
    CQRS,
    EVENT,
}
