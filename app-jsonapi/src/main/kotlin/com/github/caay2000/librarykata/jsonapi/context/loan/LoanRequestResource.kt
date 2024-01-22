package com.github.caay2000.librarykata.jsonapi.context.loan

import com.github.caay2000.common.jsonapi.InvalidJsonApiException
import com.github.caay2000.common.jsonapi.JsonApiRequestAttributes
import com.github.caay2000.common.jsonapi.JsonApiRequestResource
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema(name = "LoanRequestResource")
data class LoanRequestResource(
    @field:Schema(description = "resource type - must be `${LoanResource.TYPE}`", example = LoanResource.TYPE)
    override val type: String = LoanResource.TYPE,
    override val attributes: Attributes,
) : JsonApiRequestResource {
    @Serializable
    @Schema(name = "LoanRequestResource.Attributes")
    data class Attributes(
        @field:Schema(description = "book ISBN", example = "00000000-0000-0000-0000-000000000000")
        val bookIsbn: String,
        @field:Schema(description = "account Id", example = "00000000-0000-0000-0000-000000000000")
        val accountId: String,
    ) : JsonApiRequestAttributes

    init {
        if (type != LoanResource.TYPE) throw InvalidJsonApiException("Invalid type for LoanResource: $type")
    }
}
