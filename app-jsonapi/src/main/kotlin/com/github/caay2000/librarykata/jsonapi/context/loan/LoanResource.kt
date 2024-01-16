package com.github.caay2000.librarykata.jsonapi.context.loan

import com.github.caay2000.common.jsonapi.InvalidJsonApiException
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.common.serialization.LocalDateTimeSerializer
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class LoanResource(
    override val id: String,
    override val type: String = "loan",
    override val attributes: Attributes,
    override val relationships: Map<String, JsonApiRelationshipData>? = null,
) : JsonApiResource {
    @Serializable
    @Schema(name = "LoanResource.Attributes")
    data class Attributes(
        val bookId: String,
        val accountId: String,
        @Serializable(with = LocalDateTimeSerializer::class)
        val startLoan: LocalDateTime,
        @Serializable(with = LocalDateTimeSerializer::class)
        val finishLoan: LocalDateTime?,
    ) : JsonApiResourceAttributes

    init {
        if (type != "loan") throw InvalidJsonApiException("Invalid type for AccountResource: $type")
    }
}
