package com.github.caay2000.librarykata.hexagonal.context.application.book.search

import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookIsbn
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.SearchBookCriteria
import mu.KLogger
import mu.KotlinLogging

class SearchBookQueryHandler(bookRepository: BookRepository) : QueryHandler<SearchBookQuery, SearchBookQueryResponse> {
    override val logger: KLogger = KotlinLogging.logger {}

    private val searcher = BookSearcher(bookRepository)

    override fun handle(query: SearchBookQuery): SearchBookQueryResponse =
        SearchBookQueryResponse(
            searcher.invoke(query.toCriteria()),
        )

    private fun SearchBookQuery.toCriteria() =
        when (this) {
            is SearchBookQuery.SearchAllBookByIsbnQuery -> SearchBookCriteria.ByIsbn(BookIsbn(this.isbn))
            SearchBookQuery.SearchAllBookQuery -> SearchBookCriteria.All
        }
}

sealed class SearchBookQuery : Query {
    data object SearchAllBookQuery : SearchBookQuery()

    data class SearchAllBookByIsbnQuery(val isbn: String) : SearchBookQuery()
}

data class SearchBookQueryResponse(val values: List<Book>) : QueryResponse
