package com.github.caay2000.librarykata.core

import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.idgenerator.UUIDGenerator
import com.github.caay2000.librarykata.core.configuration.ShutdownHookConfiguration
import com.github.caay2000.librarykata.core.configuration.configureOpenApiDocumentation
import com.github.caay2000.librarykata.core.configuration.configureSerialization
import com.github.caay2000.librarykata.core.configuration.startupHookConfiguration
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.request.httpMethod
import io.ktor.server.request.receiveText
import io.ktor.util.AttributeKey
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import com.github.caay2000.librarykata.core.configuration.cqrs.DependencyInjectionConfiguration as CRQRDependencyInjectionConfiguration
import com.github.caay2000.librarykata.core.configuration.cqrs.RoutingConfiguration as CQRSRoutingConfiguration
import com.github.caay2000.librarykata.core.configuration.event.DependencyInjectionConfiguration as EventDependencyInjectionConfiguration
import com.github.caay2000.librarykata.core.configuration.event.RoutingConfiguration as EventRoutingConfiguration

fun Application.main() {
    module()
}

fun Application.module() {
    // TODO ADD HOCON Configuration to decide Architecture
    val architecture = Architecture.CQRS

    install(DoubleReceive)
    install(CallId) { generate { UUIDGenerator().generate() } }
    install(CallLogging) {
        callIdMdc("correlationId")
        // TODO print duration of request
        // check ktor callLogging format https://ktor.io/docs/call-logging.html#format and https://youtrack.jetbrains.com/issue/KTOR-1250/Log-HTTP-request-time
        logger = KotlinLogging.logger(Controller::class.java.canonicalName)
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            val callStartTime = call.attributes[AttributeKey("CallStartTime")]
            val body = runBlocking { call.receiveText() }

            "Status: $status, HTTP method: $httpMethod, User agent: $userAgent"
        }
    }
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

    configureOpenApiDocumentation()
    configureSerialization()
}

internal enum class Architecture {
    CQRS,
    EVENT,
}
