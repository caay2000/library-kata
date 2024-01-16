package com.github.caay2000.librarykata.hexagonal.context.application.book.find

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.FindBookCriteria

class BookFinder(private val bookRepository: BookRepository) {
    fun invoke(criteria: FindBookCriteria): Either<BookFinderError, Book> =
        bookRepository.find(criteria)
            .mapLeft {
                when (it) {
                    is RepositoryError.NotFoundError -> {
                        when (criteria) {
                            is FindBookCriteria.ById -> BookFinderError.BookNotFoundError(criteria.id)
                        }
                    }
                    is RepositoryError.Unknown -> BookFinderError.UnknownError(it)
                }
            }
}

sealed class BookFinderError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class UnknownError(error: Throwable) : BookFinderError(error)

    class BookNotFoundError(bookId: BookId) : BookFinderError("Book ${bookId.value} not found")
}
