package com.github.caay2000.librarykata.hexagonal.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource

class BookGroupDocumentListTransformer : Transformer<List<Book>, JsonApiDocumentList<BookGroupResource>> {
    override fun invoke(
        value: List<Book>,
        include: List<String>,
    ): JsonApiDocumentList<BookGroupResource> = value.toJsonApiBookGroupDocumentList()
}

fun List<Book>.toJsonApiBookGroupDocumentList() =
    JsonApiDocumentList(
        data = groupBy { it.isbn }.map { it.value.toJsonApiBookGroupResource() },
        included = null,
        meta = JsonApiMeta(total = groupBy { it.isbn }.size),
    )

internal fun List<Book>.toJsonApiBookGroupResource() =
    BookGroupResource(
        id = first().isbn.value,
        type = BookGroupResource.TYPE,
        attributes = toJsonApiBookGroupAttributes(),
        relationships = null,
    )

internal fun List<Book>.toJsonApiBookGroupAttributes() =
    BookGroupResource.Attributes(
        isbn = first().isbn.value,
        title = first().title.value,
        author = first().author.value,
        pages = first().pages.value,
        publisher = first().publisher.value,
        copies = size,
        availableCopies = count { it.isAvailable },
    )
