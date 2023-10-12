package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiListDocument
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.librarykata.hexagonal.context.domain.Book

data class BookByIsbnListDocument(
    override val data: List<Resource>,
    override val meta: JsonApiMeta,
) : JsonApiListDocument {

    data class Resource(
        override val id: String? = null,
        override val type: String = "book",
        override val attributes: Attributes,
        override val relationships: List<JsonApiRelationshipIdentifier> = emptyList(),
    ) : JsonApiResource {

        data class Attributes(
            val isbn: String,
            val title: String,
            val author: String,
            val pages: Int,
            val publisher: String,
            val copies: Int,
            val availableCopies: Int,
        ) : JsonApiResourceAttributes
    }
}

fun List<Book>.toBookByIsbnListDocument(): BookByIsbnListDocument {
    val groupedBooks = this.toGroupedBookByIsbnAttributes()
    return BookByIsbnListDocument(
        data = groupedBooks.map { BookByIsbnListDocument.Resource(attributes = it) },
        meta = JsonApiMeta(groupedBooks.size),
    )
}

private fun List<Book>.toGroupedBookByIsbnAttributes() =
    this.groupBy { it.isbn }
        .toSortedMap(compareBy { it.value })
        .map { (key, books) ->
            val sample = books.first()
            BookByIsbnListDocument.Resource.Attributes(
                isbn = key.value,
                title = sample.title.value,
                author = sample.author.value,
                pages = sample.pages.value,
                publisher = sample.publisher.value,
                copies = books.count(),
                availableCopies = books.count { book -> book.available.value },
            )
        }
