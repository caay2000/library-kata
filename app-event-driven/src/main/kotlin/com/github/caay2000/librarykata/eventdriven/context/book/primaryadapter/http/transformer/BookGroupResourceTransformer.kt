package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource

class BookGroupResourceTransformer : Transformer<List<Book>, BookGroupResource> {
    override fun invoke(
        value: List<Book>,
        include: List<String>,
    ): BookGroupResource =
        BookGroupResource(
            id = value.first().isbn.value,
            type = BookGroupResource.TYPE,
            attributes = value.toJsonApiBookGroupAttributes(),
            relationships = null,
        )

    private fun List<Book>.toJsonApiBookGroupAttributes() =
        BookGroupResource.Attributes(
            isbn = first().isbn.value,
            title = first().title.value,
            author = first().author.value,
            pages = first().pages.value,
            publisher = first().publisher.value,
            copies = size,
            availableCopies = count { it.isAvailable },
        )
}
