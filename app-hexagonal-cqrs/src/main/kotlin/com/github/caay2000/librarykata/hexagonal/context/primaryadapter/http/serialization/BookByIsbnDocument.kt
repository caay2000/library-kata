package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiListDocument
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource

fun List<Book>.toJsonApiListDocument(): JsonApiListDocument<BookGroupResource> {
    val groupedBooks = this.toGroupedBookByIsbnAttributes()
    return JsonApiListDocument(
        data = groupedBooks.map { BookGroupResource(id = it.isbn, attributes = it) },
        meta = JsonApiMeta(groupedBooks.size),
    )
}

private fun List<Book>.toGroupedBookByIsbnAttributes() =
    this.groupBy { it.isbn }
        .toSortedMap(compareBy { it.value })
        .map { (key, books) ->
            val sample = books.first()
            BookGroupResource.Attributes(
                isbn = key.value,
                title = sample.title.value,
                author = sample.author.value,
                pages = sample.pages.value,
                publisher = sample.publisher.value,
                copies = books.count(),
                availableCopies = books.count { book -> book.available.value },
            )
        }
