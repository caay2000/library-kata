package com.github.caay2000.librarykata.eventdriven.context.loan.application.book.availability

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.eventdriven.context.loan.application.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookAvailable
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookId

class BookAvailabilityUpdater(private val bookRepository: BookRepository) {
    fun invoke(
        bookId: BookId,
        available: BookAvailable,
    ): Either<LoanBookAvailabilityUpdaterError, Unit> =
        findBook(bookId)
            .map { book -> book.updateAvailability(available) }
            .map { book -> book.save() }

    private fun findBook(bookId: BookId): Either<LoanBookAvailabilityUpdaterError, Book> =
        bookRepository.find(bookId)
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> LoanBookAvailabilityUpdaterError.BookNotFound(bookId)
                    else -> throw error
                }
            }

    private fun Book.save() {
        bookRepository.save(this)
    }
}

sealed class LoanBookAvailabilityUpdaterError(message: String) : RuntimeException(message) {
    class BookNotFound(bookId: BookId) : LoanBookAvailabilityUpdaterError("book $bookId not found")
}
