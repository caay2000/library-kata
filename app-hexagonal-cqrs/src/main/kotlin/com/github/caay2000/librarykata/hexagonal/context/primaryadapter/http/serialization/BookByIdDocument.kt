package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.librarykata.hexagonal.context.domain.Book
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookByIdDocument(
    override val data: Resource,
    override val included: List<JsonApiIncludedResource> = emptyList(),
) : JsonApiDocument {

    @Serializable
    data class Resource(
        override val id: String,
        override val type: String = "book",
        override val attributes: Attributes,
        override val relationships: List<JsonApiRelationshipIdentifier> = emptyList(),
    ) : JsonApiResource {

        @Serializable
        @SerialName("bookById")
        data class Attributes(
            val isbn: String,
            val title: String,
            val author: String,
            val pages: Int,
            val publisher: String,
            val available: Boolean,
        ) : JsonApiResourceAttributes
    }
}

fun Book.toBookByIdDocument() = BookByIdDocument(
    data = BookByIdDocument.Resource(
        id = id.value,
        attributes = BookByIdDocument.Resource.Attributes(
            isbn = isbn.value,
            title = title.value,
            author = author.value,
            pages = pages.value,
            publisher = publisher.value,
            available = available.value,
        ),
    ),
)
