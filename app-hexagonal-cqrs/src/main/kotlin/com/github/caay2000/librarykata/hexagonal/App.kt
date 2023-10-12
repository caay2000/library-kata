package com.github.caay2000.librarykata.hexagonal

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.SerializationFeature
import com.github.caay2000.common.http.Controller
import com.github.caay2000.librarykata.hexagonal.configuration.DependencyInjectionConfiguration
import com.github.caay2000.librarykata.hexagonal.configuration.RoutingConfiguration
import com.github.caay2000.librarykata.hexagonal.configuration.ShutdownHookConfiguration
import com.github.caay2000.librarykata.hexagonal.configuration.StartupHookConfiguration
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
            configure(SerializationFeature.INDENT_OUTPUT, true)
            setDefaultPrettyPrinter(DefaultPrettyPrinter())
//            registerModule(JavaTimeModule())  // support java.time.* types
        }
//        json(
//            Json {
//                prettyPrint = true
//                isLenient = true
//
//                SerializersModule {
//                    fun PolymorphicModuleBuilder<JsonApiResourceAttributes>.registerProjectSubclasses() {
//                        subclass(LoanDocument.Resource.Attributes::class)
//                    }
//                    polymorphic(Any::class) { registerProjectSubclasses() }
//                    polymorphic(JsonApiResourceAttributes::class) { registerProjectSubclasses() }
//
//                    serializersModuleOf(UUID::class, UUIDSerializer)
//                    serializersModuleOf(LocalDate::class, LocalDateSerializer)
//                    serializersModuleOf(LocalDateTime::class, LocalDateTimeSerializer)
//                }
//            },
//        )

//        register(ContentType.JsonApi, KotlinxSerializationConverter(Json))
    }
}
