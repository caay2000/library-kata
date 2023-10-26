package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.context.book.BookByIdResource
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book

fun Book.toJsonApiDocument(): JsonApiDocument<BookByIdResource> = JsonApiDocument(
    data = BookByIdResource(
        id = id.value,
        attributes = BookByIdResource.Attributes(
            isbn = isbn.value,
            title = title.value,
            author = author.value,
            pages = pages.value,
            publisher = publisher.value,
            available = available.value,
        ),
    ),
)
