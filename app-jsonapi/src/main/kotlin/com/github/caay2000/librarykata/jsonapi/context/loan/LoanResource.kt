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
    @field:Schema(description = "loan id", example = "00000000-0000-0000-0000-000000000000")
    override val id: String,
    @field:Schema(description = "resource type - must be `$TYPE`", example = TYPE)
    override val type: String = TYPE,
    override val attributes: Attributes,
    override val relationships: Map<String, JsonApiRelationshipData>? = null,
) : JsonApiResource {
    companion object {
        const val TYPE = "loan"
    }

    @Serializable
    @Schema(name = "LoanResource.Attributes")
    data class Attributes(
        @field:Schema(description = "book id", example = "00000000-0000-0000-0000-000000000000")
        val bookId: String,
        @field:Schema(description = "account id", example = "00000000-0000-0000-0000-000000000000")
        val accountId: String,
        @field:Schema(description = "loan started date", example = "2020-01-01T00:00:00.000Z")
        @Serializable(with = LocalDateTimeSerializer::class)
        val startLoan: LocalDateTime,
        @field:Schema(description = "loan finished date", example = "2020-01-01T00:00:00.000Z")
        @Serializable(with = LocalDateTimeSerializer::class)
        val finishLoan: LocalDateTime?,
    ) : JsonApiResourceAttributes

    init {
        if (type != TYPE) throw InvalidJsonApiException("Invalid type for LoanResource: $type")
    }
}
