package com.github.caay2000.librarykata.hexagonal.context.application.book

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.BookIsbn

interface BookRepository {

    fun save(book: Book): Either<RepositoryError, Unit>
    fun find(criteria: FindBookCriteria): Either<RepositoryError, Book>
    fun search(criteria: SearchBookCriteria): Either<RepositoryError, List<Book>>
}

sealed class FindBookCriteria {
    class ById(val id: BookId) : FindBookCriteria()
}

sealed class SearchBookCriteria {
    data object All : SearchBookCriteria()
    data class ByIsbn(val isbn: BookIsbn) : SearchBookCriteria()
}
