package com.github.caay2000.librarykata.context.account.application.book.find

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.context.account.application.BookRepository
import com.github.caay2000.librarykata.context.account.domain.Book
import com.github.caay2000.librarykata.context.account.domain.BookId

class BookFinder(private val bookRepository: BookRepository) {

    fun invoke(id: BookId): Either<BookFinderError, Book> =
        bookRepository.findById(id)
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
