package com.github.caay2000.librarykata.hexagonal.context.application.book.search

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.hexagonal.context.application.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.application.book.SearchBookCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.BookIsbn
import mu.KLogger
import mu.KotlinLogging

class SearchBookByIsbnQueryHandler(bookRepository: BookRepository) : QueryHandler<SearchBookByIsbnQuery, SearchBookByIsbnQueryResponse> {

    override val logger: KLogger = KotlinLogging.logger {}

    private val searcher = BookSearcher(bookRepository)

    override fun handle(query: SearchBookByIsbnQuery): SearchBookByIsbnQueryResponse =
        searcher.invoke(SearchBookCriteria.ByIsbn(query.isbn))
            .map { books -> SearchBookByIsbnQueryResponse(books) }
            .getOrThrow()
}

data class SearchBookByIsbnQuery(val isbn: BookIsbn) : Query

data class SearchBookByIsbnQueryResponse(val value: List<Book>) : QueryResponse
