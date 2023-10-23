package com.github.caay2000.librarykata.hexagonal.configuration

import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.common.jsonapi.context.account.AccountResource
import com.github.caay2000.common.jsonapi.context.loan.LoanResource
import com.github.caay2000.common.serialization.LocalDateSerializer
import com.github.caay2000.common.serialization.LocalDateTimeSerializer
import com.github.caay2000.common.serialization.UUIDSerializer
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.serializersModuleOf
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            json = jsonMapper,
            contentType = ContentType.JsonApi,
        )
        register(ContentType.JsonApi, KotlinxSerializationConverter(jsonMapper))
    }
}

val jsonMapper = Json {
    @OptIn(ExperimentalSerializationApi::class)
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
