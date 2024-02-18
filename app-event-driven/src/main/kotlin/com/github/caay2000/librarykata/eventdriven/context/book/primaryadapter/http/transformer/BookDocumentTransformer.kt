package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource

class BookDocumentTransformer : Transformer<Book, JsonApiDocument<BookResource>> {
    private val resourceTransformer = BookResourceTransformer()

    override fun invoke(
        value: Book,
        include: List<String>,
    ): JsonApiDocument<BookResource> =
        JsonApiDocument(
            data = resourceTransformer.invoke(value),
            included = null,
        )
}
