package com.github.caay2000.common.jsonapi.context.loan

import com.github.caay2000.common.jsonapi.JsonApiRequestAttributes
import com.github.caay2000.common.jsonapi.JsonApiRequestResource
import kotlinx.serialization.Serializable

@Serializable
data class LoanRequestResource(
    override val type: String = "loan",
    override val attributes: Attributes,
) : JsonApiRequestResource {

    @Serializable
    data class Attributes(
        val bookIsbn: String,
        val accountId: String,
    ) : JsonApiRequestAttributes
}
