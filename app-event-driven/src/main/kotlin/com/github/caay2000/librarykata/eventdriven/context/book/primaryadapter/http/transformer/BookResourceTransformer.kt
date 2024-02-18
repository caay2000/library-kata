package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource

class BookResourceTransformer : Transformer<Book, BookResource> {
    override fun invoke(
        value: Book,
        include: List<String>,
    ): BookResource =
        BookResource(
            id = value.id.value,
            type = BookResource.TYPE,
            attributes = value.toJsonApiBookAttributes(),
            relationships = null,
        )

    private fun Book.toJsonApiBookAttributes() =
        BookResource.Attributes(
            isbn = isbn.value,
            title = title.value,
            author = author.value,
            pages = pages.value,
            publisher = publisher.value,
            available = available.value,
        )
}
