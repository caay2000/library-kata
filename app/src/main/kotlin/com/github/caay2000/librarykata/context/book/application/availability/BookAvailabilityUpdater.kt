package com.github.caay2000.librarykata.context.book.application.availability

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.context.book.application.BookRepository
import com.github.caay2000.librarykata.context.book.domain.Book
import com.github.caay2000.librarykata.context.book.domain.BookAvailable
import com.github.caay2000.librarykata.context.book.domain.BookId

class BookAvailabilityUpdater(private val bookRepository: BookRepository) {

    fun invoke(bookId: BookId, available: BookAvailable): Either<BookAvailabilityUpdaterError, Unit> =
        findBook(bookId)
            .map { book -> book.updateAvailability(available) }
            .flatMap { book -> book.save() }

    private fun findBook(bookId: BookId): Either<BookAvailabilityUpdaterError, Book> =
        bookRepository.findById(bookId)
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> BookAvailabilityUpdaterError.BookNotFound(bookId)
                    else -> BookAvailabilityUpdaterError.UnknownError(error)
                }
            }

    private fun Book.save(): Either<BookAvailabilityUpdaterError, Unit> =
        bookRepository.save(this)
            .mapLeft { BookAvailabilityUpdaterError.UnknownError(it) }
}

sealed class BookAvailabilityUpdaterError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class UnknownError(error: Throwable) : BookAvailabilityUpdaterError(error)
    class BookNotFound(bookId: BookId) : BookAvailabilityUpdaterError("book $bookId not found")
}
