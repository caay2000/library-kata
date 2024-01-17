package com.github.caay2000.common.test.json

import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.common.serialization.LocalDateSerializer
import com.github.caay2000.common.serialization.LocalDateTimeSerializer
import com.github.caay2000.common.serialization.UUIDSerializer
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.serializersModuleOf
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

val testJsonMapper =
    Json {
        @OptIn(ExperimentalSerializationApi::class)
        explicitNulls = false
        encodeDefaults = true
        classDiscriminator = "serializationType"
        prettyPrint = true
        isLenient = true
        val module =
            SerializersModule {
                polymorphic(JsonApiResourceAttributes::class) {
                    subclass(LoanResource.Attributes::class, LoanResource.Attributes.serializer())
                    subclass(BookResource.Attributes::class, BookResource.Attributes.serializer())
                    subclass(BookGroupResource.Attributes::class, BookGroupResource.Attributes.serializer())
                    subclass(AccountResource.Attributes::class, AccountResource.Attributes.serializer())
                }
                serializersModuleOf(UUID::class, UUIDSerializer)
                serializersModuleOf(LocalDate::class, LocalDateSerializer)
                serializersModuleOf(LocalDateTime::class, LocalDateTimeSerializer)
            }
        serializersModule = module
    }
