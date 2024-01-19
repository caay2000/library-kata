package com.github.caay2000.librarykata.event.context.account.application.book.find

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.event.context.account.application.BookRepository
import com.github.caay2000.librarykata.event.context.account.domain.Book
import com.github.caay2000.librarykata.event.context.account.domain.BookId
import mu.KLogger
import mu.KotlinLogging

class FindBookByIdQueryHandler(bookRepository: BookRepository) : QueryHandler<FindBookByIdQuery, FindBookByIdQueryResponse> {
    override val logger: KLogger = KotlinLogging.logger {}
    private val finder = BookFinder(bookRepository)

    override fun handle(query: FindBookByIdQuery): FindBookByIdQueryResponse =
        finder.invoke(query.id)
            .map { book -> FindBookByIdQueryResponse(book) }
            .getOrThrow()
}

data class FindBookByIdQuery(val id: BookId) : Query

data class FindBookByIdQueryResponse(val value: Book) : QueryResponse
