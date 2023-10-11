package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiRequestAttributes
import com.github.caay2000.common.jsonapi.JsonApiRequestDocument
import com.github.caay2000.common.jsonapi.JsonApiRequestResource
import kotlinx.serialization.Serializable

@Serializable
data class LoanRequestDocument(
    override val data: Resource,
) : JsonApiRequestDocument {

    @Serializable
    data class Resource(
        override val type: String = "loan",
        override val attributes: Attributes,
    ) : JsonApiRequestResource {

        @Serializable
        data class Attributes(
            val bookIsbn: String,
            val accountId: String,
        ) : JsonApiRequestAttributes
    }
}
