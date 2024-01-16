package com.github.caay2000.librarykata.jsonapi.context.book

import com.github.caay2000.common.jsonapi.InvalidJsonApiException
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
data class BookGroupResource(
    @field:Schema(description = "book isbn", example = "00000000-0000-0000-0000-000000000000")
    override val id: String,
    @field:Schema(description = "resource type - must be `book-group`", example = "book-group")
    override val type: String = "book-group",
    override val attributes: Attributes,
    override val relationships: Map<String, JsonApiRelationshipData>? = null,
) : JsonApiResource {
    @Serializable
    @Schema(name = "BookGroupResource.Attributes")
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
        @field:Schema(description = "total number of book copies", example = "6")
        val copies: Int,
        @field:Schema(description = "total number of book copies avilable to rent", example = "3")
        val availableCopies: Int,
    ) : JsonApiResourceAttributes

    init {
        if (type != "book-group") throw InvalidJsonApiException("Invalid type for AccountResource: $type")
    }
}
