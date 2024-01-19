package com.github.caay2000.librarykata.eventdriven.context.book.application.find

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookRepository
import mu.KLogger
import mu.KotlinLogging

class FindBookQueryHandler(bookRepository: BookRepository) : QueryHandler<FindBookQuery, FindBookQueryResponse> {
    override val logger: KLogger = KotlinLogging.logger {}

    private val finder = BookFinder(bookRepository)

    override fun handle(query: FindBookQuery): FindBookQueryResponse =
        finder.invoke(query.id)
            .map { book -> FindBookQueryResponse(book) }
            .getOrThrow()
}

data class FindBookQuery(val id: BookId) : Query

data class FindBookQueryResponse(val book: Book) : QueryResponse
