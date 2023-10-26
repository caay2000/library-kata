package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource

fun Book.toJsonApiDocument(): JsonApiDocument<BookResource> = JsonApiDocument(
    data = BookResource(
        id = id.value,
        attributes = BookResource.Attributes(
            isbn = isbn.value,
            title = title.value,
            author = author.value,
            pages = pages.value,
            publisher = publisher.value,
            available = available.value,
        ),
    ),
)
