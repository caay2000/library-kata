package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiRelationshipResource
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.librarykata.hexagonal.context.domain.Book
import kotlinx.serialization.Serializable

@Serializable
data class BookDocument(
    override val data: Resource,
    override val included: List<RelationshipResource> = emptyList(),
) : JsonApiDocument {

    @Serializable
    data class Resource(
        override val id: String,
        override val type: String = "book",
        override val attributes: Attributes,
        override val relationships: List<JsonApiRelationshipResource> = emptyList(),
    ) : JsonApiResource {

        @Serializable
        data class Attributes(
            val isbn: String,
            val title: String,
            val author: String,
            val pages: Int,
            val publisher: String,
            val available: Boolean,
        ) : JsonApiResourceAttributes
    }

    @Serializable
    data class RelationshipResource(
        override val id: String,
        override val type: String,
        override val attributes: List<JsonApiResourceAttributes>,
    ) : JsonApiRelationshipResource
}

fun Book.toBookDocument() = BookDocument(
    data = BookDocument.Resource(
        id = id.value,
        attributes = BookDocument.Resource.Attributes(
            isbn = isbn.value,
            title = title.value,
            author = author.value,
            pages = pages.value,
            publisher = publisher.value,
            available = available.value,
        ),
    ),
)
