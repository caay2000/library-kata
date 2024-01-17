package com.github.caay2000.librarykata.jsonapi.context.account

import com.github.caay2000.common.jsonapi.InvalidJsonApiException
import com.github.caay2000.common.jsonapi.JsonApiRequestAttributes
import com.github.caay2000.common.jsonapi.JsonApiRequestResource
import com.github.caay2000.common.serialization.LocalDateSerializer
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class AccountRequestResource(
    @field:Schema(description = "resource type - must be `account`", example = AccountResource.TYPE)
    override val type: String = AccountResource.TYPE,
    override val attributes: Attributes,
) : JsonApiRequestResource {
    @Serializable
    @Schema(name = "AccountRequestResource.Attributes")
    data class Attributes(
        @field:Schema(description = "user identity number", example = "B01234567")
        val identityNumber: String,
        @field:Schema(description = "user name", example = "John")
        val name: String,
        @field:Schema(description = "user surname", example = "Doe")
        val surname: String,
        @field:Schema(description = "user birthdate", example = "1970-01-01")
        @Serializable(with = LocalDateSerializer::class)
        val birthdate: LocalDate,
        @field:Schema(description = "user email", example = "john.doe@email.example")
        val email: String,
        @field:Schema(description = "user phone prefix", example = "+44")
        val phonePrefix: String,
        @field:Schema(description = "user phone number", example = "600123456")
        val phoneNumber: String,
    ) : JsonApiRequestAttributes

    init {
        if (type != AccountResource.TYPE) throw InvalidJsonApiException("Invalid type for AccountRequestResource: $type")
    }
}
