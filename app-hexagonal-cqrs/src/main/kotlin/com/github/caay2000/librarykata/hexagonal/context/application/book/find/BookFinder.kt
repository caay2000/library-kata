package com.github.caay2000.librarykata.hexagonal.context.application.book.find

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.application.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.application.book.FindBookCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.BookId

class BookFinder(private val bookRepository: BookRepository) {

    fun invoke(id: BookId): Either<BookFinderError, Book> =
        bookRepository.find(FindBookCriteria.ById(id))
            .mapLeft {
                when (it) {
                    is RepositoryError.NotFoundError -> BookFinderError.BookNotFound(id)
                    is RepositoryError.Unknown -> BookFinderError.UnknownError(it)
                }
            }
}

sealed class BookFinderError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class UnknownError(error: Throwable) : BookFinderError(error)
    class BookNotFound(bookId: BookId) : BookFinderError("Book ${bookId.value} not found")
}
