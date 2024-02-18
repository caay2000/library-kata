package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.querybus.Query
import com.github.caay2000.common.querybus.QueryBusHandler
import com.github.caay2000.common.querybus.QueryResponse
import com.github.caay2000.librarykata.eventdriven.context.book.application.find.FindBookQuery
import com.github.caay2000.librarykata.eventdriven.context.book.application.find.FindBookQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookRepository
import mu.KLogger
import mu.KotlinLogging

class BookResourceQueryBusHandler(bookRepository: BookRepository) : QueryBusHandler<BookResourceQuery, BookResourceQueryResponse> {
    private val bookQueryHandler = FindBookQueryHandler(bookRepository)
    private val bookResourceTransformer = BookResourceTransformer()

    override val logger: KLogger = KotlinLogging.logger {}

    override fun handle(query: BookResourceQuery): BookResourceQueryResponse {
        val account = retrieveBook(query)
        return BookResourceQueryResponse(bookResourceTransformer.invoke(account))
    }

    private fun retrieveBook(query: BookResourceQuery): Book =
        when (query) {
            is BookResourceQuery.ByIdentifier -> bookQueryHandler.invoke(FindBookQuery(BookId(query.identifier))).book
            is BookResourceQuery.ByValue -> query.value
        }
}

sealed class BookResourceQuery : Query {
    data class ByIdentifier(val identifier: String) : BookResourceQuery()

    data class ByValue(val value: Book) : BookResourceQuery()
}

data class BookResourceQueryResponse(val resource: JsonApiResource) : QueryResponse
