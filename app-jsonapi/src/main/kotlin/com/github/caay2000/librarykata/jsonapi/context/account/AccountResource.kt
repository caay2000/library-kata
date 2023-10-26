package com.github.caay2000.librarykata.jsonapi.context.account

import com.github.caay2000.common.jsonapi.InvalidJsonApiException
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.common.serialization.LocalDateSerializer
import com.github.caay2000.common.serialization.LocalDateTimeSerializer
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class AccountResource(
    @field:Schema(description = "account id", example = "00000000-0000-0000-0000-000000000000")
    override val id: String,
    @field:Schema(description = "resource type - must be `account`", example = "account")
    override val type: String = "account",
    override val attributes: Attributes,
    override val relationships: Map<String, JsonApiRelationshipData>? = null,
) : JsonApiResource {

    @Serializable
    @Schema(name = "AccountResource.Attributes")
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
        @field:Schema(description = "user phone prefix", example = "+44")
        val phoneNumber: String,
        @field:Schema(description = "user registration date", example = "2020-01-01T00:00:00.000Z")
        @Serializable(with = LocalDateTimeSerializer::class)
        val registerDate: LocalDateTime,
    ) : JsonApiResourceAttributes

    init {
        if (type != "account") throw InvalidJsonApiException("Invalid type for AccountResource: $type")
    }
}
