package com.github.caay2000.librarykata.hexagonal

import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.serialization.defaultJacksonConfiguration
import com.github.caay2000.common.serialization.defaultObjectMapper
import com.github.caay2000.librarykata.hexagonal.configuration.DependencyInjectionConfiguration
import com.github.caay2000.librarykata.hexagonal.configuration.RoutingConfiguration
import com.github.caay2000.librarykata.hexagonal.configuration.ShutdownHookConfiguration
import com.github.caay2000.librarykata.hexagonal.configuration.StartupHookConfiguration
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import mu.KotlinLogging
import java.util.UUID

fun Application.main() {
    module()
}

fun Application.module() {
    install(CallId) { generate { UUID.randomUUID().toString() } }
    install(CallLogging) {
        callIdMdc("correlationId")
        logger = KotlinLogging.logger(Controller::class.java.canonicalName)
    }
    install(StartupHookConfiguration)
    install(ShutdownHookConfiguration)
    install(DependencyInjectionConfiguration)
    install(RoutingConfiguration)
    configureSerialization()
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            defaultJacksonConfiguration()
        }
        register(ContentType.JsonApi, JacksonConverter(defaultObjectMapper()))
    }
}
