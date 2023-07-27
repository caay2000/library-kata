package com.github.caay2000.projectskeleton.context.book.application.search

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.projectskeleton.common.cqrs.Query
import com.github.caay2000.projectskeleton.common.cqrs.QueryHandler
import com.github.caay2000.projectskeleton.common.cqrs.QueryResponse
import com.github.caay2000.projectskeleton.context.book.application.BookRepository
import com.github.caay2000.projectskeleton.context.book.application.SearchBookCriteria
import com.github.caay2000.projectskeleton.context.book.domain.Book
import com.github.caay2000.projectskeleton.context.book.domain.BookIsbn

class SearchBookByIsbnQueryHandler(bookRepository: BookRepository) : QueryHandler<SearchBookByIsbnQuery, SearchBookByIsbnQueryResponse> {

    private val searcher = BookSearcher(bookRepository)

    override fun handle(query: SearchBookByIsbnQuery): SearchBookByIsbnQueryResponse =
        searcher.invoke(SearchBookCriteria.ByIsbn(query.isbn))
            .map { books -> SearchBookByIsbnQueryResponse(books) }
            .getOrThrow()
}

data class SearchBookByIsbnQuery(val isbn: BookIsbn) : Query

data class SearchBookByIsbnQueryResponse(val value: List<Book>) : QueryResponse
