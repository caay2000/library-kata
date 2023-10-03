package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiAttributes
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiListDocument
import com.github.caay2000.common.jsonapi.JsonApiRelationshipResource
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.librarykata.hexagonal.context.domain.Book
import kotlinx.serialization.Serializable

@Serializable
data class BookViewDocument(
    override val data: BookViewResource,
    override val included: List<BookViewRelationshipResource> = emptyList(),
) : JsonApiDocument

@Serializable
data class BookViewListDocument(override val data: List<BookViewResource>) : JsonApiListDocument

@Serializable
data class BookViewResource(
    override val id: String? = null,
    override val type: String = "book",
    override val attributes: BookViewAttributes,
    override val relationships: List<JsonApiRelationshipResource> = emptyList(),
) : JsonApiResource

@Serializable
data class BookViewAttributes(
    val isbn: String,
    val title: String,
    val author: String,
    val pages: Int,
    val publisher: String,
    val copies: Int,
    val availableCopies: Int,
) : JsonApiAttributes

@Serializable
data class BookViewRelationshipResource(
    override val id: String,
    override val type: String,
    override val attributes: List<JsonApiAttributes>,
) : JsonApiRelationshipResource

fun List<Book>.toBookViewDocument() = BookViewDocument(
    data = BookViewResource(
        attributes = toGroupedBookViewAttributes().first(),
    ),
)

fun List<Book>.toBookViewListDocument() = BookViewListDocument(
    data = this.toGroupedBookViewAttributes().map {
        BookViewResource(attributes = it)
    },
)

private fun List<Book>.toGroupedBookViewAttributes() =
    this.groupBy { it.isbn }
        .toSortedMap(compareBy { it.value })
        .map { (key, books) ->
            val sample = books.first()
            BookViewAttributes(
                isbn = key.value,
                title = sample.title.value,
                author = sample.author.value,
                pages = sample.pages.value,
                publisher = sample.publisher.value,
                copies = books.count(),
                availableCopies = books.count { book -> book.available.value },
            )
        }
