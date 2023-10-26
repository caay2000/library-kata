package com.github.caay2000.librarykata.jsonapi.context.loan

import com.github.caay2000.common.jsonapi.InvalidJsonApiException
import com.github.caay2000.common.jsonapi.JsonApiRequestAttributes
import com.github.caay2000.common.jsonapi.JsonApiRequestResource
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
data class LoanRequestResource(
    override val type: String = "loan",
    override val attributes: Attributes,
) : JsonApiRequestResource {

    @Serializable
    @Schema(name = "LoanRequestResource.Attributes")
    data class Attributes(
        val bookIsbn: String,
        val accountId: String,
    ) : JsonApiRequestAttributes

    init {
        if (type != "loan") throw InvalidJsonApiException("Invalid type for AccountResource: $type")
    }
}
