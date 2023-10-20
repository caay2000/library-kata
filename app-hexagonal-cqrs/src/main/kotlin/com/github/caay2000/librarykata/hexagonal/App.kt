package com.github.caay2000.librarykata.hexagonal

import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.idgenerator.UUIDGenerator
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.common.jsonapi.context.account.AccountResource
import com.github.caay2000.common.jsonapi.context.loan.LoanResource
import com.github.caay2000.common.serialization.LocalDateSerializer
import com.github.caay2000.common.serialization.LocalDateTimeSerializer
import com.github.caay2000.common.serialization.UUIDSerializer
import com.github.caay2000.librarykata.hexagonal.configuration.DependencyInjectionConfiguration
import com.github.caay2000.librarykata.hexagonal.configuration.RoutingConfiguration
import com.github.caay2000.librarykata.hexagonal.configuration.ShutdownHookConfiguration
import com.github.caay2000.librarykata.hexagonal.configuration.StartupHookConfiguration
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.serializersModuleOf
import mu.KotlinLogging
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

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
    configureSerialization()
}

val jsonMapper = Json {
    explicitNulls = false
    encodeDefaults = true
    classDiscriminator = "serializationType"
    prettyPrint = true
    isLenient = true
    val module = SerializersModule {
        polymorphic(JsonApiResourceAttributes::class) {
            subclass(LoanResource.Attributes::class, LoanResource.Attributes.serializer())
            subclass(AccountResource.Attributes::class, AccountResource.Attributes.serializer())
        }
        serializersModuleOf(UUID::class, UUIDSerializer)
        serializersModuleOf(LocalDate::class, LocalDateSerializer)
        serializersModuleOf(LocalDateTime::class, LocalDateTimeSerializer)
    }
    serializersModule = module
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            json = jsonMapper,
            contentType = ContentType.JsonApi,
        )
        register(ContentType.JsonApi, KotlinxSerializationConverter(jsonMapper))
    }
}
