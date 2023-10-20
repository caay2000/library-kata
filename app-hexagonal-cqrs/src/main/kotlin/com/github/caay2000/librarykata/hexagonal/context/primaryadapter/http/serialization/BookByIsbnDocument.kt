package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiListDocument
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.common.jsonapi.context.book.BookByIsbnResource
import com.github.caay2000.librarykata.hexagonal.context.domain.Book

fun List<Book>.toJsonApiListDocument(): JsonApiListDocument<BookByIsbnResource> {
    val groupedBooks = this.toGroupedBookByIsbnAttributes()
    return JsonApiListDocument(
        data = groupedBooks.map { BookByIsbnResource(id = it.isbn, attributes = it) },
        meta = JsonApiMeta(groupedBooks.size),
    )
}

private fun List<Book>.toGroupedBookByIsbnAttributes() =
    this.groupBy { it.isbn }
        .toSortedMap(compareBy { it.value })
        .map { (key, books) ->
            val sample = books.first()
            BookByIsbnResource.Attributes(
                isbn = key.value,
                title = sample.title.value,
                author = sample.author.value,
                pages = sample.pages.value,
                publisher = sample.publisher.value,
                copies = books.count(),
                availableCopies = books.count { book -> book.available.value },
            )
        }
