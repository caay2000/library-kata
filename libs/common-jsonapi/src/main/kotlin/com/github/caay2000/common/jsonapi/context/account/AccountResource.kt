package com.github.caay2000.common.jsonapi.context.account

import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.common.serialization.LocalDateSerializer
import com.github.caay2000.common.serialization.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class AccountResource(
    override val id: String,
    override val type: String = "account",
    override val attributes: Attributes,
    override val relationships: Map<String, JsonApiRelationshipData>? = null,
) : JsonApiResource {

    @Serializable
    @SerialName("account")
    data class Attributes(
        val identityNumber: String,
        val name: String,
        val surname: String,
        @Serializable(with = LocalDateSerializer::class)
        val birthdate: LocalDate,
        val email: String,
        val phonePrefix: String,
        val phoneNumber: String,
        @Serializable(with = LocalDateTimeSerializer::class)
        val registerDate: LocalDateTime,
    ) : JsonApiResourceAttributes
}
