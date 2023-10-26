package com.github.caay2000.librarykata.hexagonal.context.application.book.search

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookIsbn
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.SearchBookCriteria
import mu.KLogger
import mu.KotlinLogging

class SearchBooksQueryHandler(bookRepository: BookRepository) : QueryHandler<SearchBooksQuery, SearchAllBooksQueryResponse> {

    override val logger: KLogger = KotlinLogging.logger {}

    private val searcher = BookSearcher(bookRepository)

    override fun handle(query: SearchBooksQuery): SearchAllBooksQueryResponse =
        query.toCriteria().let { criteria ->
            searcher.invoke(criteria)
                .map { books -> SearchAllBooksQueryResponse(books) }
                .getOrThrow()
        }

    private fun SearchBooksQuery.toCriteria() =
        when (this) {
            is SearchBooksQuery.SearchAllBooksByIsbnQuery -> SearchBookCriteria.ByIsbn(BookIsbn(this.isbn))
            SearchBooksQuery.SearchAllBooksQuery -> SearchBookCriteria.All
        }
}

sealed class SearchBooksQuery : Query {
    data object SearchAllBooksQuery : SearchBooksQuery()
    data class SearchAllBooksByIsbnQuery(val isbn: String) : SearchBooksQuery()
}

data class SearchAllBooksQueryResponse(val values: List<Book>) : QueryResponse
