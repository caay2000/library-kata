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
@Schema(name = "AccountResource")
data class AccountResource(
    @field:Schema(description = "account id", example = "00000000-0000-0000-0000-000000000000")
    override val id: String,
    @field:Schema(description = "resource type - must be `$TYPE`", example = TYPE)
    override val type: String = TYPE,
    override val attributes: Attributes,
    override val relationships: Map<String, JsonApiRelationshipData>? = null,
) : JsonApiResource {
    companion object {
        const val TYPE = "account"
    }

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
        @field:Schema(description = "user phone (prefix and number)", example = "+44 123456789")
        val phone: String,
        @field:Schema(description = "user registration date", example = "2020-01-01T00:00:00.000Z")
        @Serializable(with = LocalDateTimeSerializer::class)
        val registerDate: LocalDateTime,
        @field:Schema(description = "current user loans", example = "2")
        val currentLoans: Int,
        @field:Schema(description = "lifetime user loans", example = "13")
        val totalLoans: Int,
    ) : JsonApiResourceAttributes

    init {
        if (type != TYPE) throw InvalidJsonApiException("Invalid type for AccountResource: $type")
    }
}
