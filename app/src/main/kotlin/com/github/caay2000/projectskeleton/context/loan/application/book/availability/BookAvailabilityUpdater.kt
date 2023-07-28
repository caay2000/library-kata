package com.github.caay2000.projectskeleton.context.loan.application.book.availability

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.projectskeleton.context.loan.application.BookRepository
import com.github.caay2000.projectskeleton.context.loan.domain.Book
import com.github.caay2000.projectskeleton.context.loan.domain.BookAvailable
import com.github.caay2000.projectskeleton.context.loan.domain.BookId

class BookAvailabilityUpdater(private val bookRepository: BookRepository) {

    fun invoke(bookId: BookId, available: BookAvailable): Either<LoanBookAvailabilityUpdaterError, Unit> =
        findBook(bookId)
            .map { book -> book.updateAvailability(available) }
            .flatMap { book -> book.save() }

    private fun findBook(bookId: BookId): Either<LoanBookAvailabilityUpdaterError, Book> =
        bookRepository.findById(bookId)
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> LoanBookAvailabilityUpdaterError.BookNotFound(bookId)
                    else -> LoanBookAvailabilityUpdaterError.UnknownError(error)
                }
            }

    private fun Book.save(): Either<LoanBookAvailabilityUpdaterError, Unit> =
        bookRepository.save(this)
            .mapLeft { LoanBookAvailabilityUpdaterError.UnknownError(it) }
}

sealed class LoanBookAvailabilityUpdaterError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class UnknownError(error: Throwable) : LoanBookAvailabilityUpdaterError(error)
    class BookNotFound(bookId: BookId) : LoanBookAvailabilityUpdaterError("book $bookId not found")
}
