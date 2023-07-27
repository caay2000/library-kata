package com.github.caay2000.projectskeleton.context.book.application.find

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.projectskeleton.common.cqrs.Query
import com.github.caay2000.projectskeleton.common.cqrs.QueryHandler
import com.github.caay2000.projectskeleton.common.cqrs.QueryResponse
import com.github.caay2000.projectskeleton.context.book.application.BookRepository
import com.github.caay2000.projectskeleton.context.book.domain.Book
import com.github.caay2000.projectskeleton.context.book.domain.BookId

class FindBookByIdQueryHandler(bookRepository: BookRepository) : QueryHandler<FindBookByIdQuery, FindBookByIdQueryResponse> {

    private val finder = BookFinder(bookRepository)

    override fun handle(query: FindBookByIdQuery): FindBookByIdQueryResponse =
        finder.invoke(query.id)
            .map { book -> FindBookByIdQueryResponse(book) }
            .getOrThrow()
}

data class FindBookByIdQuery(val id: BookId) : Query

data class FindBookByIdQueryResponse(val value: Book) : QueryResponse
