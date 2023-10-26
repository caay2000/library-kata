package com.github.caay2000.librarykata.jsonapi.context.book

import com.github.caay2000.common.jsonapi.InvalidJsonApiException
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
data class BookGroupResource(
    override val id: String,
    override val type: String = "book-group",
    override val attributes: Attributes,
    override val relationships: Map<String, JsonApiRelationshipData>? = null,
) : JsonApiResource {

    @Serializable
    @Schema(name = "BookGroupResource.Attributes")
    data class Attributes(
        val isbn: String,
        val title: String,
        val author: String,
        val pages: Int,
        val publisher: String,
        val copies: Int,
        val availableCopies: Int,
    ) : JsonApiResourceAttributes

    init {
        if (type != "book-group") throw InvalidJsonApiException("Invalid type for AccountResource: $type")
    }
}
