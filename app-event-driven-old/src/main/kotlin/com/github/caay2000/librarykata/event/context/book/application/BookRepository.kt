package com.github.caay2000.librarykata.event.context.book.application

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.event.context.book.domain.Book
import com.github.caay2000.librarykata.event.context.book.domain.BookId
import com.github.caay2000.librarykata.event.context.book.domain.BookIsbn

interface BookRepository {
    fun save(book: Book)

    fun search(criteria: SearchBookCriteria): List<Book>

    fun find(id: BookId): Either<RepositoryError, Book>
}

sealed class SearchBookCriteria {
    object All : SearchBookCriteria()

    data class ByIsbn(val isbn: BookIsbn) : SearchBookCriteria()
}
