package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.query.ResourceQuery
import com.github.caay2000.common.query.ResourceQueryHandler
import com.github.caay2000.common.query.ResourceQueryResponse
import com.github.caay2000.librarykata.eventdriven.context.book.application.find.FindBookQuery
import com.github.caay2000.librarykata.eventdriven.context.book.application.find.FindBookQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookRepository
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import mu.KLogger
import mu.KotlinLogging

class BookResourceQueryBusHandler(bookRepository: BookRepository) : ResourceQueryHandler {
    private val bookQueryHandler = FindBookQueryHandler(bookRepository)
    private val bookResourceTransformer = BookResourceTransformer()

    override val logger: KLogger = KotlinLogging.logger {}
    override val type: String = BookResource.TYPE

    override fun handle(query: ResourceQuery): ResourceQueryResponse {
        val account = retrieveBook(query)
        return ResourceQueryResponse(bookResourceTransformer.invoke(account))
    }

    private fun retrieveBook(query: ResourceQuery): Book = bookQueryHandler.invoke(FindBookQuery(BookId(query.identifier))).book
}
