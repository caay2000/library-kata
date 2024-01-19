package com.github.caay2000.librarykata.event.context.book.application.search

import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.event.context.book.application.BookRepository
import com.github.caay2000.librarykata.event.context.book.application.SearchBookCriteria
import com.github.caay2000.librarykata.event.context.book.domain.Book
import com.github.caay2000.librarykata.event.context.book.domain.BookIsbn
import mu.KLogger
import mu.KotlinLogging

class SearchBookByIsbnQueryHandler(bookRepository: BookRepository) : QueryHandler<SearchBookByIsbnQuery, SearchBookByIsbnQueryResponse> {
    override val logger: KLogger = KotlinLogging.logger {}

    private val searcher = BookSearcher(bookRepository)

    override fun handle(query: SearchBookByIsbnQuery): SearchBookByIsbnQueryResponse = SearchBookByIsbnQueryResponse(searcher.invoke(SearchBookCriteria.ByIsbn(query.isbn)))
}

data class SearchBookByIsbnQuery(val isbn: BookIsbn) : Query

data class SearchBookByIsbnQueryResponse(val value: List<Book>) : QueryResponse
