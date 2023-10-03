package com.github.caay2000.librarykata.eventdriven.context.book.application.search

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.eventdriven.context.book.application.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.book.application.SearchBookCriteria
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import mu.KLogger
import mu.KotlinLogging

class SearchAllBooksQueryHandler(bookRepository: BookRepository) : QueryHandler<SearchAllBooksQuery, SearchAllBooksQueryResponse> {

    override val logger: KLogger = KotlinLogging.logger {}

    private val searcher = BookSearcher(bookRepository)

    override fun handle(
        @Suppress("UNUSED_PARAMETER") query: SearchAllBooksQuery,
    ): SearchAllBooksQueryResponse =
        searcher.invoke(SearchBookCriteria.All)
            .map { books -> SearchAllBooksQueryResponse(books) }
            .getOrThrow()
}

class SearchAllBooksQuery : Query

data class SearchAllBooksQueryResponse(val values: List<Book>) : QueryResponse
