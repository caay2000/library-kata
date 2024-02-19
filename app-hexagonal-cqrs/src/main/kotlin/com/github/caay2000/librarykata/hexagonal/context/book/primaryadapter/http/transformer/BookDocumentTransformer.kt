package com.github.caay2000.librarykata.hexagonal.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource

class BookDocumentTransformer() : Transformer<Book, JsonApiDocument<BookResource>> {
    override fun invoke(
        value: Book,
        include: List<String>,
    ): JsonApiDocument<BookResource> = value.toJsonApiBookDocument()
}

fun Book.toJsonApiBookDocument() =
    JsonApiDocument(
        data = toJsonApiBookResource(),
        included = null,
    )

internal fun Book.toJsonApiBookResource() =
    BookResource(
        id = id.value,
        type = BookResource.TYPE,
        attributes = toJsonApiBookAttributes(),
        relationships = null,
    )

internal fun Book.toJsonApiBookAttributes() =
    BookResource.Attributes(
        isbn = isbn.value,
        title = title.value,
        author = author.value,
        pages = pages.value,
        publisher = publisher.value,
        available = available.value,
    )
