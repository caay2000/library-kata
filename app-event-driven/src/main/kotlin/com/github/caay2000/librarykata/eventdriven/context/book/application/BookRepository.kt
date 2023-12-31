package com.github.caay2000.librarykata.eventdriven.context.book.application

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookIsbn

interface BookRepository {

    fun save(book: Book): Either<RepositoryError, Unit>
    fun search(criteria: SearchBookCriteria): Either<RepositoryError, List<Book>>

    fun findById(id: BookId): Either<RepositoryError, Book>
}

sealed class SearchBookCriteria {
    object All : SearchBookCriteria()
    data class ByIsbn(val isbn: BookIsbn) : SearchBookCriteria()
}
