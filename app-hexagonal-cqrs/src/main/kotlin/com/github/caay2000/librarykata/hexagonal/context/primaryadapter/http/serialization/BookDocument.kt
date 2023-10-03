package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiAttributes
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiRelationshipResource
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.librarykata.hexagonal.context.domain.Book
import kotlinx.serialization.Serializable

@Serializable
data class BookDocument(
    override val data: BookResource,
    override val included: List<BookRelationshipResource> = emptyList(),
) : JsonApiDocument

@Serializable
data class BookResource(
    override val id: String,
    override val type: String = "book",
    override val attributes: BookAttributes,
    override val relationships: List<JsonApiRelationshipResource> = emptyList(),
) : JsonApiResource

@Serializable
data class BookAttributes(
    val isbn: String,
    val title: String,
    val author: String,
    val pages: Int,
    val publisher: String,
    val available: Boolean,
) : JsonApiAttributes

@Serializable
data class BookRelationshipResource(
    override val id: String,
    override val type: String,
    override val attributes: List<JsonApiAttributes>,
) : JsonApiRelationshipResource

fun Book.toBookDocument() = BookDocument(
    data = BookResource(
        id = id.value,
        attributes = BookAttributes(
            isbn = isbn.value,
            title = title.value,
            author = author.value,
            pages = pages.value,
            publisher = publisher.value,
            available = available.value,
        ),
    ),
)
