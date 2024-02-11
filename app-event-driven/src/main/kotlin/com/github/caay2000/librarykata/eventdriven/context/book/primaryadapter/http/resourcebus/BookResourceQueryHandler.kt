package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.resourcebus

import com.github.caay2000.common.resourcebus.ResourceBusQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.book.application.find.FindBookQuery
import com.github.caay2000.librarykata.eventdriven.context.book.application.find.FindBookQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.transformer.toJsonApiBookResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource

class BookResourceQueryHandler(
    bookRepository: BookRepository,
) : ResourceBusQueryHandler<BookResource> {
    private val queryHandler = FindBookQueryHandler(bookRepository)

    override fun retrieve(
        identifier: String,
        includeRelationships: Boolean,
    ): BookResource = queryHandler.invoke(FindBookQuery(BookId(identifier))).book.toJsonApiBookResource()
}
