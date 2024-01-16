package com.github.caay2000.librarykata.jsonapi.context.book

import com.github.caay2000.common.jsonapi.InvalidJsonApiException
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
data class BookResource(
    @field:Schema(description = "book id", example = "00000000-0000-0000-0000-000000000000")
    override val id: String,
    @field:Schema(description = "resource type - must be `book`", example = "book")
    override val type: String = "book",
    override val attributes: Attributes,
    override val relationships: Map<String, JsonApiRelationshipData>? = null,
) : JsonApiResource {
    @Serializable
    @Schema(name = "BookResource.Attributes")
    data class Attributes(
        @field:Schema(description = "book ISBN", example = "00000000-0000-0000-0000-000000000000")
        val isbn: String,
        @field:Schema(description = "book title", example = "Life of John Doe")
        val title: String,
        @field:Schema(description = "book author", example = "John Doe")
        val author: String,
        @field:Schema(description = "book pages", example = "90")
        val pages: Int,
        @field:Schema(description = "book publisher", example = "John Doe Publishing Inc.")
        val publisher: String,
        @field:Schema(description = "book availability", example = "true", allowableValues = ["true", "false"])
        val available: Boolean,
    ) : JsonApiResourceAttributes

    init {
        if (type != "book") throw InvalidJsonApiException("Invalid type for AccountResource: $type")
    }
}
